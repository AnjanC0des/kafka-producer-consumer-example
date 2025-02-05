package myConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;

public class Consumer {
	public void start() {
		String bootstrapserver="127.0.0.1:9092";
		String topic="signal";
		String group="consumergroup";
		Properties props=new Properties();
		props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapserver);
		props.setProperty(ConsumerConfig.GROUP_ID_CONFIG,group);
		props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());	
		props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
		props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		
		final Thread mainThread=Thread.currentThread();
		KafkaConsumer<String,String> consumer=new KafkaConsumer<String,String>(props);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				consumer.wakeup();
				try {
					mainThread.join();
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		
		try {
			consumer.subscribe(Arrays.asList(topic));
			while(true) {
				ConsumerRecords<String,String> records=consumer.poll(Duration.ofMillis(100));
				for(ConsumerRecord<String,String> record:records) {
					System.out.println(record.partition()+" "+record.key()+" "+record.value());
				}
			}
		}
		catch(WakeupException e) {
			e.printStackTrace();
		}catch(Exception e) { 	
			e.printStackTrace();
		}finally {
			consumer.close();
			System.out.println("SHUTDOWN");
		}
	}
}
