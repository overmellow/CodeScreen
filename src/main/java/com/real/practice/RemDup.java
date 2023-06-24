package com.real.practice;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RemDup {

	public static void main(String[] args) {
		List<Integer> withDupes = Arrays.asList(10, 10, 20, 20, 30, 30, 40, 50);

		System.out.println("List with duplicates: " + withDupes);

		List<Integer> withoutDupes = withDupes.stream()
		                                      .distinct()
		                                      .collect(Collectors.toList());

		System.out.println("List without duplicates: " + withoutDupes);

	}

}
