package env;

import java.util.ArrayList;

public class Cue {

    private static ArrayList<Cue> values;

    public static Cue get(String cueName){
        if (values == null)
            values = new ArrayList<Cue>();
        
        for (Cue value : values) {
            if (value.name.equals(cueName))
                return value;
        }
        Cue cue = new Cue(cueName);
        values.add(cue);
        return cue; 
    }

    public int ordinal(){
        return values.indexOf(this);
    }

    private String name;

    public String getName() {
        return name;
    }

    private Cue(String name) {
		this.name = name;
	}

    public static Cue[] values() {
        Cue[] dummy = new Cue[0];
        return values.toArray(dummy);
    }

    @Override
	public String toString() {
		return name;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cue cue = (Cue) o;

        if (name != null ? !name.equals(cue.name) : cue.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
