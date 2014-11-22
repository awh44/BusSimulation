import java.util.concurrent.Semaphore;

public class Student implements Runnable
{
	Bus bus_;
	BusStop stop_;

	public Student(Bus bus, BusStop stop)
	{
		bus_ = bus;
		stop_ = stop;
	}

	@Override
	public void run()
	{
		if (wait_and_acquire_seat())
		{
			board_bus();
		}
		else
		{
			System.out.println("Student missed the last bus.");
		}
	}

	private boolean wait_and_acquire_seat()
	{
		System.out.println("Student waiting for bus.");
		return stop_.wait_for_bus();
	}

	private void board_bus()
	{
		System.out.println("Student boarding the bus.");
		bus_.board();
		stop_.boarded();
	}
}
