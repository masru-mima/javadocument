package test;

import java.util.ArrayList;
import java.util.List;

public class LambdaExpression {
	public void methodA() {
		List<String> cities = new ArrayList<>();
		cities.add("東京");
		cities.add("横浜");
		cities.forEach( city -> System.out.println(city) );
		cities.forEach( System.out::println );
	}

}
