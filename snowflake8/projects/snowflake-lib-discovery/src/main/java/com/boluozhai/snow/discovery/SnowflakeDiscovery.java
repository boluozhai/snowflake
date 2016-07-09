package com.boluozhai.snow.discovery;

public class SnowflakeDiscovery {

	public static void main(String[] arg) {
		SnowflakeDiscovery sd = new SnowflakeDiscovery();
		sd.loop();
	}

	private void loop() {

		String tag = this.getClass().getSimpleName();

		for (int cnt = 0; cnt < 100; cnt++) {

			long time = System.currentTimeMillis();
			System.out.format("%s[%d], time=%d\n", tag, cnt, time);

			this.sleep(1000);

		}

	}

	private void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
