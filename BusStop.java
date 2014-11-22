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
			//Atomically, using the mutex_, determine whether the bus has departed for the final
			//time, and if it has, return false to indicate that. Otherwise, increase the number of
			//students waiting.
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

			//Decrease the number of students waiting by 1.
			change_waiting(-1);
		}
		catch (InterruptedException e)
		{
			System.out.println("Caught an InterruptedException. Exiting.");
			System.exit(1);
		}

		//if the student has acquired a seat on the bus, return true
		return true;
	}

	public void boarded()
	{
		//Indicate to the bus that a student has boarded.
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
			//take the mininum of the number of students waiting and the number of seats...
			int waiting = waiting_;
			int max_seats = bus.get_max_seats();
			int release = waiting < max_seats ? waiting : max_seats;

			System.out.println("Bus arrived. Number of students to board: " + release);

			//...and release that many students to board.
			board_.release(release); 
			//Also force the bus to acquire that many students before it can depart, i.e., let all
			//the students who are boarding board first.
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
			//Wait for any threads in the mutex in the wait_for_bus method to finish modifying
			//waiting before setting buses_remain to false
			mutex_.acquire();
			buses_remain_ = false;
			mutex_.release();

			System.out.println(waiting_ + " waiting students missed the last bus.");
			//Then release all the students who are waiting.
			board_.release(waiting_);
		}
		catch (InterruptedException e)
		{
			System.out.println("Caught an InterruptedException. Exiting.");
			System.exit(1);
		}
	}
}
