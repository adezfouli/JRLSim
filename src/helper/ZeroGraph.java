package helper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import flanagan.roots.RealRoot;
import flanagan.roots.RealRootFunction;

public class ZeroGraph {
	public static void main(String[] args) throws FileNotFoundException {
		RealRoot realroot = new RealRoot();
		PrintWriter CuStream = new PrintWriter("/media/AmirHDD/Science/Addiction/Research/ResponseRate/optimalReponseRate/plot-data/cu.plot");
		PrintWriter CVStream = new PrintWriter("/media/AmirHDD/Science/Addiction/Research/ResponseRate/optimalReponseRate/plot-data/cv.plot");
		PrintWriter RStream = new PrintWriter("/media/AmirHDD/Science/Addiction/Research/ResponseRate/optimalReponseRate/plot-data/R.plot");
		ResponseRateEquation equation = null;
		
		equation = new ResponseRateEquation(1, 1, 2, 10);
		for (double Cu = 0.1 ; Cu < 5; Cu+=0.01){
			equation.setCu(Cu);
			double r = realroot.bisect(equation,0,30);
			CuStream.write(Cu + ", "+r+"\n");
		}
			
		equation = new ResponseRateEquation(1, 1, 2, 10);
		for (double Cv = 0.1 ; Cv < 5; Cv+=0.01){
			equation.setCv(Cv);
			double r = realroot.bisect(equation,0,30);
			CVStream.write(Cv + ", "+r+ "\n");
		}
		
		equation = new ResponseRateEquation(1, 1, 2, 10);
		for (double R = 10 ; R < 20; R+=0.01){
			equation.setR(R);
			double r = realroot.bisect(equation,0,30);
			RStream.write(R + ", "+r+"\n");
		}
		
		CuStream.close();
		CVStream.close();
		RStream.close();
		
		System.out.println(equation.appRoot());
	}
}

class ResponseRateEquation implements RealRootFunction {

	public double Cu, Cv, lambda, R;

	public ResponseRateEquation(double cu, double cv, double lambda, double r) {
		super();
		Cu = cu;
		Cv = cv;
		this.lambda = lambda;
		R = r;
	}

	public double function() {
		return function(2*Cv/ (R-Cu));
	}
	
	public double appRoot(){
		return 2*Cv/ (R-Cu);
	}

	public double function(double a) {
		return a * Cu + 2 * Cv - a * R + a * a * R * lambda
				* Math.exp(-lambda * a) + a * R * Math.exp(-lambda * a);
//		return a*a -4;
	}

	public double getCu() {
		return Cu;
	}

	public void setCu(double cu) {
		Cu = cu;
	}

	public double getCv() {
		return Cv;
	}

	public void setCv(double cv) {
		Cv = cv;
	}

	public double getLambda() {
		return lambda;
	}

	public void setLambda(double lambda) {
		this.lambda = lambda;
	}

	public double getR() {
		return R;
	}

	public void setR(double r) {
		R = r;
	}
	
}
