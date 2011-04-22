package ioio.tests.torture;

import java.util.List;
import java.util.Random;

import ioio.lib.api.IOIO;
import android.app.Activity;
import android.widget.TextView;

class TestProvider {
	private final IOIO ioio_;
	private final ResourceAllocator alloc_;
	private final PassFailTestAggregator dioAgg_;
	private final PassFailTestAggregator aiAgg_;
	private final PassFailTestAggregator pwmAgg_;
	private final PassFailTestAggregator uartAgg_;
	private final HistogramAggregator dlatAgg_;
	private final Random random_ = new Random(1);

	public TestProvider(Activity activity, IOIO ioio, ResourceAllocator alloc) {
		ioio_ = ioio;
		alloc_ = alloc;
		dioAgg_ = new PassFailTestAggregator(activity,
				(TextView) activity.findViewById(R.id.digital_io_pass),
				(TextView) activity.findViewById(R.id.digital_io_fail),
				(TextView) activity.findViewById(R.id.digital_io_count));
		aiAgg_ = new PassFailTestAggregator(activity,
				(TextView) activity.findViewById(R.id.analog_input_pass),
				(TextView) activity.findViewById(R.id.analog_input_fail),
				(TextView) activity.findViewById(R.id.analog_io_count));
		pwmAgg_ = new PassFailTestAggregator(activity,
				(TextView) activity.findViewById(R.id.pwm_pass),
				(TextView) activity.findViewById(R.id.pwm_fail),
				(TextView) activity.findViewById(R.id.pwm_count));
		uartAgg_ = new PassFailTestAggregator(activity,
				(TextView) activity.findViewById(R.id.uart_pass),
				(TextView) activity.findViewById(R.id.uart_fail),
				(TextView) activity.findViewById(R.id.uart_count));
		dlatAgg_ = new HistogramAggregator(activity, new TextView[] {
				(TextView) activity.findViewById(R.id.digital_latency_min),
				(TextView) activity.findViewById(R.id.digital_latency_p25),
				(TextView) activity.findViewById(R.id.digital_latency_p50),
				(TextView) activity.findViewById(R.id.digital_latency_p75),
				(TextView) activity.findViewById(R.id.digital_latency_max) },
				(TextView) activity.findViewById(R.id.digital_latency_count));
	}

	public synchronized TestRunner newTest() throws InterruptedException {
		int selection = random_.nextInt(5);
		switch (selection) {
		case 0:
			return new TypedTestRunner<Boolean>(
					new DigitalIOTest(ioio_, alloc_), dioAgg_);
		case 1:
			return new TypedTestRunner<List<Float>>(new DigitalLatencyTest(
					ioio_, alloc_), dlatAgg_);
		case 2:
			return new TypedTestRunner<Boolean>(new AnalogInputTest(ioio_,
					alloc_), aiAgg_);
		case 3:
			return new TypedTestRunner<Boolean>(new PwmTest(
					ioio_, alloc_), pwmAgg_);
		case 4:
			return new TypedTestRunner<Boolean>(new UartTest(
					ioio_, alloc_), uartAgg_);
		}
		return null;
	}
}
