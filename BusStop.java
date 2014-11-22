import java.util.concurrent.Semaphore;

public class BusStop
{
	private Semaphore board_ = new Semaphore(0);
	private Semaphore can_depart_ = new Semaphore(0);
	private Semaphore mutex_ = new Semaphore(1, true);
	private int waiting_ = 0;
	private boolean buses_remain_ = true;

	public boolean wait_for_bus()
	{
		try
		{
			mutex_.acquire();
			if (!buses_remain_)
			{
				mutex_.release();
				return false;
			}
			waiting_++;
			mutex_.release();

			board_.acquire();
			if (!buses_remain_)
			{
				return false;
			}

			change_waiting(-1);
		}
		catch (InterruptedException e)
		{
			System.out.println("Caught an InterruptedException. Exiting.");
			System.exit(1);
		}

		return true;
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

	public void no_remaining_buses() 
	{
		try
		{
			mutex_.acquire();
			buses_remain_ = false;
			mutex_.release();

			System.out.println(waiting_ + " waiting students missed the last bus.");
			board_.release(waiting_);
		}
		catch (InterruptedException e)
		{
			System.out.println("Caught an InterruptedException. Exiting.");
			System.exit(1);
		}
	}
}
