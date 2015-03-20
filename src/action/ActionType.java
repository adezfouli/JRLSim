package action;

import java.io.Serializable;
import java.util.ArrayList;

public class ActionType implements Serializable {

	private static ArrayList<ActionType> values;

	private String name;


    public String getName() {
        return name;
    }

    public static ActionType get(String actionName){
        if (values == null)
            values = new ArrayList<ActionType>();
        for (ActionType value : values) {
            if (value.name.equals(actionName))
                return value;
        }
        ActionType actionType = new ActionType(actionName);
        values.add(actionType);
        return actionType;
    }

    public int ordinal(){
        return values.indexOf(this);
    }

    public ActionType(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

    private static ActionType[] values() {
        return values.toArray(new ActionType[0]);
    }
}
