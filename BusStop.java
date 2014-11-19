import java.util.concurrent.Semaphore;

public class BusStop
{
	private Semaphore board_ = new Semaphore(0);
	private Semaphore can_depart_ = new Semaphore(0);
	private Semaphore mutex_ = new Semaphore(1, true);
	private int waiting_ = 0;

	public void wait_for_bus()
	{
		try
		{
			change_waiting(1);

			board_.acquire();
			change_waiting(-1);
		}
		catch (InterruptedException e)
		{
			System.out.println("Caught an InterruptedException. Exiting.");
			System.exit(1);
		}
	}

	public void boarded()
	{
		can_depart_.release();
	}

	private void change_waiting(int diff) throws InterruptedException
	{
		mutex_.acquire();
		waiting_ += diff;
		mutex_.release();
	}

	public void arrive_and_wait(Bus bus)
	{
		try
		{
			int waiting = waiting_;
			int max_seats = bus.get_max_seats();
			int release = waiting < max_seats ? waiting : max_seats;

			board_.release(release);
			can_depart_.acquire(release);	
		}
		catch (InterruptedException e)
		{
			System.out.println("Caught an InterruptedException. Exiting.");
			System.exit(1);
		}
	}
}
