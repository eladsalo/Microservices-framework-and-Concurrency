
// to-do: change names


package bgu.spl.mics;

import java.util.Queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */

	public class MessageBusImpl implements MessageBus {

		private static class MessageBusHolder {
			private static MessageBusImpl instance = new MessageBusImpl();
		}


	private ConcurrentHashMap<MicroService,BlockingQueue<Message>> assignMap;
	// for each MicroService will let you the queue of all the  messages that he need to done

	private ConcurrentHashMap<MicroService, Queue<Class<? extends Message>>> subscribeMap;
	// for each MicroService will let you the queue of all the kind of messages he subscribe to
	// (that he subscribe by event and by broadcast)

	private ConcurrentHashMap<Class<? extends Event>,Queue<MicroService>> subscribeEventMap;
	// for each kind of massages will let you the queue of all the MicroService that subscribe to this event

	private ConcurrentHashMap<Class<? extends Broadcast>,Queue<MicroService>> subscribeBroadcastMap;
	// for each kind of massages will let you the queue of all the MicroService that subscribe to this Broadcast

	private ConcurrentHashMap<Event,Future> futureMap;

	private MessageBusImpl(){
		assignMap = new ConcurrentHashMap<>();
		subscribeEventMap=new ConcurrentHashMap<>();
		subscribeBroadcastMap=new ConcurrentHashMap<>();
		futureMap=new ConcurrentHashMap<>();
		subscribeMap = new ConcurrentHashMap<>();
	}
	public static MessageBusImpl getInstance() {
		return MessageBusHolder.instance;
	}


	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		synchronized (subscribeEventMap.getClass()) {
			subscribeEventMap.putIfAbsent(type, new ConcurrentLinkedQueue<>());
		}
		subscribeEventMap.get(type).add(m);

		if(subscribeMap.get(m).contains(type)==false)
			subscribeMap.get(m).add(type);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		synchronized (subscribeBroadcastMap.getClass()) {
			subscribeBroadcastMap.putIfAbsent(type, new ConcurrentLinkedQueue<>());
		}
		subscribeBroadcastMap.get(type).add(m);

		if(subscribeMap.get(m).contains(type)==false)
			subscribeMap.get(m).add(type);
	}

	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		synchronized (e) {
			Future future = futureMap.get(e);
			if (future != null) {
				future.resolve(result);
				futureMap.remove(e);
			}
		}

	}

	@Override
	public void sendBroadcast(Broadcast b) {
		Queue<MicroService> queue = subscribeBroadcastMap.get(b.getClass());
		if(queue != null) {
			for(MicroService m : queue) {
				Queue<Message> messageQueue =assignMap.get(m);
				if(messageQueue != null)
					messageQueue.add(b);

			}
		}
	}

	@Override @SuppressWarnings("unchecked")
	public <T> Future<T> sendEvent(Event<T> e) {
		MicroService m=null;
		Future<T> f=null;
		Queue<MicroService> queue=subscribeEventMap.get(e.getClass());
		if (queue != null) {
			synchronized (e.getClass()) {
				m=subscribeEventMap.get(e.getClass()).poll();
				if (m != null)
					queue.add(m);
			}
			if (m != null) {
				synchronized (m) {
					Queue<Message> messages = assignMap.get(m);
					if(messages != null) {
						f=new Future<>();
						futureMap.put(e, f);
						messages.add(e);
					}
				}
			}
		}
		return f;
	}

	@Override
	public void register(MicroService m) {
		assignMap.putIfAbsent(m, new LinkedBlockingQueue<>());
		subscribeMap.putIfAbsent(m, new ConcurrentLinkedQueue<>());
	}


	@Override @SuppressWarnings("unchecked")
	public void unregister(MicroService m) {
		Queue<Message> messageQueue;
		synchronized (m) {
			messageQueue=assignMap.remove(m);
		}
		for(Class<? extends Message> message : subscribeMap.get(m)) {
			if(Event.class.isAssignableFrom(message))
				subscribeEventMap.get(message).remove(m);
			else if(Broadcast.class.isAssignableFrom(message))
				subscribeBroadcastMap.get(message).remove(m);

		}
		subscribeMap.remove(m);
		for(Message e : messageQueue)
			complete((Event)e, null);
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		return assignMap.get(m).take();
	}
}