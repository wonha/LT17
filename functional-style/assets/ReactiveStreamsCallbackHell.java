package com.wonha.scratchspring5tobytv1.ep5;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * @author wonha.shin
 */
public class ReactiveStreamsCallbackHell {
	@Test
	public void startCallbackHell() {
		ExecutorService es = Executors.newSingleThreadExecutor();
		Publisher<String> publisher = new Publisher<String>() {
			List<String> data = Arrays.asList("a", "b", "c", "d", "e");
			@Override
			public void subscribe(Subscriber s) {
				s.onSubscribe(new Subscription() {
					int itr = 0;
					@Override
					public void request(long n) {
						es.execute(() -> {
							int i = 0;
							try {
								while (i < n) {
									if (i < data.size()) {
										System.out.println("Sending " + data.get(itr));
										s.onNext(data.get(itr));
									} else {
										s.onComplete();
										break;
									}
									itr++;
									i++;
								}
							} catch (IndexOutOfBoundsException e) {
								s.onComplete();
							} catch (Throwable t) {
								s.onError(t);
							}
						});
					}
					@Override
					public void cancel() {}
				});
			}
		};
	};
}
