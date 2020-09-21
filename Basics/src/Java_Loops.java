
public class Java_Loops {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// 1.) Create array

		int numbers[] = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

		// 2.) Create a loop
		for (int num : numbers) {
			System.out.println(num);
		}

		System.out.println("\n");

		// 3.) Print only evens
		for (int i = 0; i < numbers.length; i++) {
			if (numbers[i] % 2 == 0) {
				System.out.println(numbers[i]);
			}
		}

		System.out.println("\n");

		// 4.) Explain how this works
		System.out.println(
				"Question 3 works as we are testing to see if there is a remainder when an element is divided by 2. If there is "
						+ "no remainder, we out put the element");

	}

}
