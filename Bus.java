import java.util.concurrent.Semaphore;

public class Bus implements Runnable
{
	private BusStop stop_;
	private int max_;
	private int num_loops_;
	private int drive_time_;
	private int boarded_;
	private Semaphore mutex_ = new Semaphore(1, true);

	public Bus(BusStop stop, int max_students, int num_loops, int drive_time)
	{
		stop_ = stop;
		max_ = max_students;
		num_loops_ = num_loops;
		drive_time_ = drive_time;
	}

	public void board()
	{
		try
		{
			mutex_.acquire();
			boarded_++;	
			mutex_.release();
		}
		catch (InterruptedException e)
		{
			System.out.println("Caught an InterruptedException. Exiting.");
			System.exit(1);
		}	
	}

	public int get_max_seats()
	{
		return max_;
	}

	@Override
	public void run()
	{
		for (int i = 0; i < num_loops_; i++)
		{
			arrive();
			depart();
		}	
	}

	private void arrive()
	{
		boarded_ = 0;
		stop_.arrive_and_wait(this);
	}

	private void depart()
	{
		System.out.println("Bus departing. Number of students boarded: " + boarded_);
		try
		{
			Thread.sleep(drive_time_);
		}
		catch (InterruptedException e)
		{
			System.out.println("Caught an InterruptedException. Exiting.");
			System.exit(1);
		}
	}
}
