package com.evri.interview;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestUtils {

	@SafeVarargs
	public <T> List<T> listOf(T... elements) {
		return Arrays.stream(elements).collect(Collectors.toList());
	}

}
