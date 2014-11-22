public class Main
{
	public static void main(String[] args)
	{
		if (args.length < 4)
		{
			System.out.println("Usage: java Main [number of students] [max seats on bus] [number of bus loops] [drive time for bus]");
			return;
		}

		int num_students, max_seats, num_loops, drive_time;
		try
		{
			num_students = Integer.parseInt(args[0]);
			max_seats = Integer.parseInt(args[1]);
			num_loops = Integer.parseInt(args[2]);
			drive_time = Integer.parseInt(args[3]);
		}
		catch (NumberFormatException e)
		{
			System.out.println("Please include only integers as the command line arguments.");
			return;
		}


		BusStop stop = new BusStop();
		Bus bus = new Bus(stop, max_seats, num_loops, drive_time);
		Thread bus_thread = new Thread(bus);
		Thread student_threads[] = new Thread[num_students];

		int first = num_students / 2;
		for (int i = 0; i < first; i++)
		{
			student_threads[i] = new Thread(new Student(bus, stop));
			student_threads[i].start();
		}

		bus_thread.start();	

		for (int i = first; i < num_students; i++)
		{
			student_threads[i] = new Thread(new Student(bus, stop));
			student_threads[i].start();
		}

		try
		{
			bus_thread.join();
			for (int i = 0; i < num_students; i++)
			{
				student_threads[i].join();
			}
		}
		catch (InterruptedException e)
		{
			System.out.println("Caught an InterruptedException. Exiting.");
			System.exit(1);
		}
	}
}
