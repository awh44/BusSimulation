public class Main
{
	public static void main(String[] args)
	{
		BusStop stop = new BusStop();
		Bus bus = new Bus(stop, 50, 30, 1);
		Thread bus_thread = new Thread(bus);

		for (int i = 0; i < 300; i++)
		{
			new Thread(new Student(bus, stop)).start();
		}

		bus_thread.start();

		for (int i = 0; i < 300; i++)
		{
			new Thread(new Student(bus, stop)).start();
			Thread.yield();
		}	

		try
		{
			bus_thread.join();
		}
		catch (InterruptedException e)
		{
			System.out.println("Caught an InterruptedException. Exiting.");
			System.exit(1);
		}
	}
}
