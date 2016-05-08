package my.draw.flowchart;

import my.draw.core.Box;
import my.draw.core.VerticalBox;

public class IfBox extends VerticalBox {
	private Box condition;
	
	public IfBox() {
		this(null);
	}
	
	public Box getCondition() {
		return condition;
	}
	
	public IfBox(Box parent) {
		super(parent);
	}
	
	public void addCondition(String conditionLabel) {
		if( conditionLabel == null ) {
			conditionLabel = "alt: 上記以外";
		}
		else {
			conditionLabel = "alt: " + conditionLabel;
		}
		this.condition = new Box(conditionLabel);
		super.add(condition);
	}
	
	public void add(Box body) {
		this.condition.add(body);
	}
	
}
