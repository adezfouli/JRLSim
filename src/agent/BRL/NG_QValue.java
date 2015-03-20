package agent.BRL;

import java.awt.Toolkit;
import java.io.Serializable;

import agent.Agent;
import agent.ProbQValue;
import agent.QValue;

import cern.jet.random.engine.DRand;
import reward.BRL_RewardValues;
import reward.CocaineAndOponentReward;
import reward.CocaineReward;
import reward.Reward;

import umontreal.iro.lecuyer.util.Num;
import cern.jet.random.HyperGeometric;
import cern.jet.random.Normal;
import cern.jet.random.StudentT;
import cern.jet.stat.Gamma;
import flanagan.integration.IntegralFunction;
import flanagan.integration.Integration;
import flanagan.roots.RealRoot;
import flanagan.roots.RealRootFunction;

public class NG_QValue extends ProbQValue {

    private static final long serialVersionUID = 1693420645910692286L;

    public double landa;

    public double mu;

    public double alpha;

    public double beta;

    private static double epsilon = 1e-2;

    private static Alpha alphaF = new Alpha();

    private static RealRoot realRoot = new RealRoot();

    {
        realRoot.setmaximumStaticBoundsExtension(1);
        realRoot.setLowerBound(1);
        realRoot.setUpperBound(3000);
    }

    public NG_QValue(Agent agent, double mu, double landa, double alpha,
                     double beta) {
        super(agent);
        this.landa = landa;
        this.mu = mu;
        this.alpha = alpha;
        this.beta = beta;
    }

    public double Emu() {
        return mu;
    }

    public double moment2() {
        return (landa + 1) / landa * beta / (alpha - 1) + mu * mu;
    }

    @Override
    public double update(Reward r, QValue q, double df, double delay) {
        // return mixtureUpdate(r, (NG_QValue)q, df, delay);
        return momentUpdate(r.getMagnitude(), (NG_QValue) q, df, delay);
    }

    public double update(double r, QValue q, double df, double delay) {
        // return mixtureUpdate(r, (NG_QValue)q, df, delay);
        return momentUpdate(r, (NG_QValue) q, df, delay);
    }

    public double momentUpdate(double r, QValue q, double df, double delay) {

        df = Math.pow(df, delay);
//	System.out.println(r + " " + mu + " df : " + df + " " + q.Emu());
        double n = 1;
        double M1 = r + df * q.Emu();
        double M2 = r * r + 2 * df * r * q.Emu() + df * df
                * ((NG_QValue) q).moment2();

        update(n, M1, M2);

        return r;
    }

    private void update(double n, double M1, double M2) {
        beta += (n / 2) * (M2 - M1 * M1) + n * landa * (M1 - mu) * (M1 - mu)
                / (2 * (landa + n));
        mu = (landa * mu + n * M1) / (landa + n);
        landa += n;
        alpha += n / 2;
    }

    public double update(double r) {
        double n = 1;
        double M1 = r;
        double M2 = r * r;
        update(n, M1, M2);
        return r;
    }

    public void update(NG_QValue dummy, double r) {

        double n = 1;
        double M1 = r;
        double M2 = r * r;

        dummy.beta = beta + (n / 2) * (M2 - M1 * M1) + n * landa * (M1 - mu)
                * (M1 - mu) / (2 * (landa + n));
        dummy.mu = (landa * mu + n * M1) / (landa + n);
        dummy.landa = landa + n;
        dummy.alpha = alpha + n / 2;
    }

    public double update(double[] r) {
        double n = r.length;
        double M1 = 0;
        for (int i = 0; i < r.length; i++) {
            M1 += r[i];
        }
        M1 /= n;

        double M2 = 0;
        for (int i = 0; i < r.length; i++) {
            M2 += r[i] * r[i];
        }
        M2 /= n;

        update(n, M1, M2);
        return M1;
    }

    public double update(double[] r, QValue q, double df, double delay) {
        df = Math.pow(df, delay);
        double n = r.length;
        double M1 = 0;
        for (int i = 0; i < r.length; i++) {
            M1 += r[i] + df * q.Emu();
        }
        M1 /= n;

        double M2 = 0;
        for (int i = 0; i < r.length; i++) {
            M2 += r[i] * r[i] + 2 * df * r[i] * q.Emu() + df * df
                    * ((NG_QValue) q).moment2();
        }
        M2 /= n;

        update(n, M1, M2);
        return M1;
    }

    public double getDrugInducedReward(Reward reward, QValue q, double df,
                                       double delay) {
        double dft = Math.pow(df, delay);
        double r = reward.getMagnitude();
        if (reward instanceof CocaineReward) {
            double Ds = ((CocaineReward) reward).getDS();
            double basalRewardLevel = agent.getNonExploratoryAverageReward();
            double delta = Math.max(
                    (reward.getMagnitude() - basalRewardLevel + dft * q.Emu())
                            - mu + Ds, Ds - basalRewardLevel);
            r = delta - dft * q.Emu() + mu;
        } else {
            r = reward.getMagnitude() - agent.getNonExploratoryAverageReward();
        }
        return r;
    }

    public double mixtureUpdate(double r, NG_QValue q, double df,
                                double delay) {

        df = Math.pow(df, delay);
        double eTau = UtterIntegral.calc(new Tau(r, df, this, q));
        alphaF.root = Math.log(eTau)
                - UtterIntegral.calc(new LogTau(r, df, this, q));
        double mu2Tau = UtterIntegral.calc(new Mu2Tau(r, df, this, q));
        NG_QValue temp = (NG_QValue) this.copy();
        this.update(temp, r);
        mu = UtterIntegral.calc(new MuTau(r, df, this, q)) / eTau;
        landa = (double) 1 / (mu2Tau - eTau * mu * mu);
        if (alphaF.root > Math.log(1) - Num.digamma(1))
            alpha = 1 + epsilon;
        else
            alpha = realRoot.bisect(alphaF);
        beta = alpha / eTau;
        return r;
    }

    @Override
    public String toString() {
        return "\nMean Mu : " + mu + " Var Mu : " + beta / landa / (alpha - 1)
                + " Mean Tau : " + Etau() + " \n" + "alpha : " + alpha
                + " beta : " + beta + " mu : " + mu + " landa : " + landa;
    }

    public double cdf(double x) {
        return new StudentT(2 * alpha, new DRand()).cdf((x - mu)
                * Math.sqrt(landa * alpha / beta));
    }

    @Override
    public double VarMu() {
        return beta / landa / (alpha - 1);
    }

    public double varReward() {
        return beta * (landa + 1) / (landa) / (alpha - 1);
    }

    public QValue copy() {
        return new NG_QValue(agent, mu, landa, alpha, beta);
    }

    public double getRandomMu() {
        return new cern.jet.random.Normal(mu, Math.sqrt((double) 1
                / (landa * getRandomTau())), new DRand()).nextDouble();
    }

    public double getRandomTau() {
        return new cern.jet.random.Gamma(alpha, (double) 1 / beta,
                new DRand()).nextDouble();
    }

    public double Etau() {
        return (alpha / beta);
    }

    public double getNormalPdf(double x) {
        return getNoramlPdf(Emu(), (double) 1 / (Etau() * landa), x);
    }

    public double getPdf(double mu, double tau) {
        return getNoramlPdf(this.mu, (double) 1 / (tau * landa), mu)
                * getGammaPdf(alpha, beta, tau);
    }

    public static double getNoramlPdf(double mu, double var, double x) {
        return ((double) 1 / Math.sqrt(2 * Math.PI * var))
                * Math.exp(-(x - mu) * (x - mu) / 2 / var);
    }

    public static double getGammaPdf(double alpha, double beta, double x) {
        return Math.pow(beta, alpha) / Gamma.gamma(alpha)
                * Math.pow(x, alpha - 1) * Math.exp(-beta * x);
    }

    public double pdfMu(double x) {
        return Math.sqrt((landa / (double) 2 / Math.PI))
                * Math.pow(beta, alpha)
                * Gamma.gamma(alpha + 0.5)
                / Gamma.gamma(alpha)
                * Math.pow(beta + (double) 1 / 2 * landa * (x - mu) * (x - mu),
                -alpha - 0.5);
    }

    public double pdfTau(double x) {
        return getGammaPdf(alpha, beta, x);
    }

    private static ZSumIntegral zIntegral = new ZSumIntegral();

    /**
     * returns distribution at point x = mu , y = tau1 * tau2 / (tau1 + tau2), z =
     * tau2
     *
     * @param v1
     * @param v2
     * @param x
     * @param y
     * @param z
     * @return
     */
    public static double getPdfOfSumOfTwoNG(NG_QValue v1, NG_QValue v2,
                                            double x, double y, double z) {
        double mu = x, tau2 = z, tau1 = z * y / (z - y);

        double f = sumOfTwoNormal(v1, v2, mu, tau1, tau2);

        return f * (tau1 * tau1 + 2 * tau1 * tau2 + tau2 * tau2)
                / (tau2 * tau2);
    }

    /**
     * Suppose we have two random variable r1 = N(m1, 1 / tau1), r2 = N(m2, 1 /
     * tau2) where taus are inverse of variances. And v1 = p(m1, tau1) and v2 =
     * p(m2,tau2) Then if we suppose r1 + r2 = N(m3, 1/tau3) then this function
     * return p(m3,tau3)
     *
     * @param v1
     * @param v2
     * @param mu
     * @param tau
     * @return
     */
    public static double getPdfOfSumOfTwoNG(NG_QValue v1, NG_QValue v2,
                                            double mu, double tau) {
        zIntegral.x = mu;
        zIntegral.y = tau;
        zIntegral.v1 = v1;
        zIntegral.v2 = v2;
        return Integration.gaussQuad(zIntegral, 0, 100, UtterIntegral.glPoints);
    }

    public static QValue getSumOfTwoNG(NG_QValue v1, NG_QValue v2) {
        NG_QValue v = new NG_QValue(v1.getAgent(), -1, -1, -1, -1);
        double eTau = UtterIntegral.calc(new ZTau(v1, v2));
        alphaF.root = Math.log(eTau) - UtterIntegral.calc(new ZLogTau(v1, v2));
        double mu2Tau = UtterIntegral.calc(new ZMu2Tau(v1, v2));
        v.mu = UtterIntegral.calc(new ZMuTau(v1, v2)) / eTau;
        v.landa = (double) 1 / (mu2Tau - eTau * v.mu * v.mu);
        if (alphaF.root > Math.log(1) - Num.digamma(1))
            v.alpha = 1 + epsilon;
        else
            v.alpha = realRoot.bisect(alphaF);
        v.beta = v.alpha / eTau;
        return v;
    }

    /**
     * the function returns joint distribution of (mu,tau1, tau2) where mu = mu1 +
     * mu2
     *
     * @param v1
     * @param v2
     * @param mu
     * @param tau1
     * @param tau2
     * @return
     */
    public static double sumOfTwoNormal(NG_QValue v1, NG_QValue v2, double mu,
                                        double tau1, double tau2) {
        return NG_QValue.getNoramlPdf(v1.mu + v2.mu, 1D / v1.landa / tau1 + 1D
                / v2.landa / tau2, mu)
                * NG_QValue.getGammaPdf(v1.alpha, v1.beta, tau1)
                * NG_QValue.getGammaPdf(v2.alpha, v2.beta, tau2);
    }

    public static void main(String[] args) {

        // alpha : 24.5 beta : 52.55453184286 mu : 9.797394102692094 landa :
        // 46.0
        // alpha : 23.5 beta : 443.72566579983817 mu : -29.285774444812162 landa
        // : 44.0

        NG_QValue v1 = new NG_QValue(null, 9.7, 46, 24.5, 52.5), v2 = new NG_QValue(
                null, -29, 44, 23.5, 443), zero = new NG_QValue(null, 0, 3, 3,
                3);
        // for (int i=0; i < 40; i++){
        // v1.momentUpdate(new BRL_RewardValues().foodEating(), zero, 1,1);
        // System.out.println(v1.Emu());
        // }

        System.out.println("V2");
        for (int i = 0; i < 20; i++) {
//	    v1.momentUpdate(new BRL_RewardValues().foodEating(), v2, 1, 1);
            System.out.println(v1.Emu());
        }
    }

    public void setMu(double mu) {
        this.mu = mu;
    }
}

class UtterIntegral implements IntegralFunction {

    public static int glPoints = 200;

    public double function(double tau) {
        return InnerIngegral.calc(tau);
    }

    public static UtterIntegral utterIntegral = new UtterIntegral();

    public static double calc(MuTauFunction h) {
        double lowerLimit = 0;
        double upperLimit = 40;
        InnerIngegral.h = h;
        return Integration.gaussQuad(utterIntegral, lowerLimit, upperLimit,
                glPoints);
    }
}

class MixtureIntegral implements IntegralFunction {

    public static NG_QValue p1, p2,
            dummy = new NG_QValue(null, -1, -1, -1, -1);

    public static double r;

    public static double df;

    public static double mu, tau;

    public double function(double x) {
        p1.update(dummy, r + df * x);
        return dummy.getPdf(mu, tau) * p2.getNormalPdf(x);
    }

    public static MixtureIntegral mixtureIntegral = new MixtureIntegral();

    public static double calc(double r, double df, NG_QValue p1, NG_QValue p2,
                              double mu, double tau) {
        MixtureIntegral.r = r;
        MixtureIntegral.df = df;
        MixtureIntegral.p1 = p1;
        MixtureIntegral.p2 = p2;
        MixtureIntegral.mu = mu;
        MixtureIntegral.tau = tau;
        double lowerLimit = -40;
        double upperLimit = 40;
        // System.out.println(df);
        if (df < 0.0001) {
            NG_QValue k = (NG_QValue) p1.copy();
            p1.update(k, r);
            return k.getPdf(mu, tau);
        } else
            return Integration.gaussQuad(mixtureIntegral, lowerLimit,
                    upperLimit, UtterIntegral.glPoints);
    }

}

class InnerIngegral implements IntegralFunction {

    public static double tau;

    public static NG_QValue v;

    public static MuTauFunction h;

    public double function(double mu) {
        if (v != null) {
            return h.h(mu, tau) * v.getPdf(mu, tau);
        } else
            return h.h(mu, tau);
    }

    public static InnerIngegral innerIngegral = new InnerIngegral();

    private static double lowerLimit = -40;

    private static double upperLimit = 40;

    public static double calc(double tau) {
        InnerIngegral.tau = tau;
        return Integration.gaussQuad(innerIngegral, lowerLimit, upperLimit,
                UtterIntegral.glPoints);
    }
}

abstract class MuTauFunction {
    public abstract double h(double mu, double tau);
}

class MixtureMuTau extends MuTauFunction {

    private double r;

    private double df;

    private NG_QValue p1;

    private NG_QValue p2;

    public MixtureMuTau(double r, double df, NG_QValue p1, NG_QValue p2) {
        this.r = r;
        this.df = df;
        this.p1 = p1;
        this.p2 = p2;
    }

    public double h(double mu, double tau) {
        return MixtureIntegral.calc(r, df, p1, p2, mu, tau);
    }
}

class MuTau extends MixtureMuTau {

    public MuTau(double r, double df, NG_QValue p1, NG_QValue p2) {
        super(r, df, p1, p2);
    }

    public double h(double mu, double tau) {
        return mu * tau * super.h(mu, tau);
    }
}

class Mu2Tau extends MixtureMuTau {
    public Mu2Tau(double r, double df, NG_QValue p1, NG_QValue p2) {
        super(r, df, p1, p2);
    }

    public double h(double mu, double tau) {
        return mu * mu * tau * super.h(mu, tau);
    }
}

class Tau extends MixtureMuTau {

    public Tau(double r, double df, NG_QValue p1, NG_QValue p2) {
        super(r, df, p1, p2);
    }

    public double h(double mu, double tau) {
        return tau * super.h(mu, tau);
    }
}

class Mu2 extends MixtureMuTau {

    public Mu2(double r, double df, NG_QValue p1, NG_QValue p2) {
        super(r, df, p1, p2);
    }

    public double h(double mu, double tau) {
        if (Math.abs(mu * mu * super.h(mu, tau)) > Double.MAX_VALUE - 10)
            System.out.println("amir");
        return mu * mu * super.h(mu, tau);
    }
}

class Tau2 extends MixtureMuTau {

    public Tau2(double r, double df, NG_QValue p1, NG_QValue p2) {
        super(r, df, p1, p2);
    }

    public double h(double mu, double tau) {
        return tau * tau * super.h(mu, tau);
    }
}

class LogTau extends MixtureMuTau {

    public LogTau(double r, double df, NG_QValue p1, NG_QValue p2) {
        super(r, df, p1, p2);
    }

    public double h(double mu, double tau) {
        return Math.log(tau) * super.h(mu, tau);
    }
}

class TestF implements IntegralFunction {

    public double function(double x) {
        return x * x * NG_QValue.getGammaPdf(3, 3, x);
    }
}

class Alpha implements RealRootFunction {
    public double root;

    public double function(double y) {
        return Math.log(y) - Num.digamma(y) - root;
    }
}

class ZSumIntegral implements IntegralFunction {

    public double x, y;

    public NG_QValue v1, v2;

    public double function(double z) {
        if (z < y)
            return 0;
        return NG_QValue.getPdfOfSumOfTwoNG(v1, v2, x, y, z);
    }
}

abstract class ZIntegral extends MuTauFunction {

    NG_QValue v1, v2;

    public ZIntegral(NG_QValue v1, NG_QValue v2) {
        super();
        this.v1 = v1;
        this.v2 = v2;
    }

    @Override
    public abstract double h(double mu, double tau);

}

class ZMu extends ZIntegral {

    public ZMu(NG_QValue v1, NG_QValue v2) {
        super(v1, v2);
    }

    @Override
    public double h(double mu, double tau) {
        return mu * NG_QValue.getPdfOfSumOfTwoNG(v1, v2, mu, tau);
    }
}

class ZMuTau extends ZIntegral {

    public ZMuTau(NG_QValue v1, NG_QValue v2) {
        super(v1, v2);
    }

    @Override
    public double h(double mu, double tau) {
        return tau * mu * NG_QValue.getPdfOfSumOfTwoNG(v1, v2, mu, tau);
    }
}

class ZMu2Tau extends ZIntegral {

    public ZMu2Tau(NG_QValue v1, NG_QValue v2) {
        super(v1, v2);
    }

    @Override
    public double h(double mu, double tau) {
        return tau * mu * mu * NG_QValue.getPdfOfSumOfTwoNG(v1, v2, mu, tau);
    }
}

class ZTau extends ZIntegral {

    public ZTau(NG_QValue v1, NG_QValue v2) {
        super(v1, v2);
    }

    @Override
    public double h(double mu, double tau) {
        return tau * NG_QValue.getPdfOfSumOfTwoNG(v1, v2, mu, tau);
    }
}

class ZLogTau extends ZIntegral {

    public ZLogTau(NG_QValue v1, NG_QValue v2) {
        super(v1, v2);
    }

    @Override
    public double h(double mu, double tau) {
        return Math.log(tau) * NG_QValue.getPdfOfSumOfTwoNG(v1, v2, mu, tau);
    }
}

