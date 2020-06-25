// The MIT License (MIT)
// Copyright © 2015 AppsLandia. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.appslandia.sweetsop.http;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import com.appslandia.common.utils.IOUtils;

/**
 * 
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 */
public class Downloader {

	protected void download(final int index, final String url, final Handler handler) throws IOException {
		final HttpClient client = HttpClient.get(url);
		handler.initialize(index, url, client);
		client.execute();

		if (client.getResultObject() == null) {
			throw new IOException(client.getResponseMessage());
		}
	}

	public void download(final int fromIndex, final int toIndex, final Handler handler) {
		final int totalCount = toIndex - fromIndex + 1;
		final AtomicInteger successCount = new AtomicInteger(0);

		final AtomicInteger countDown = new AtomicInteger(totalCount);
		final ExecutorService executorService = Executors.newFixedThreadPool(handler.getPoolThreads());

		for (int index = fromIndex; index <= toIndex; index++) {
			final int downloadIndex = index;
			final String url = handler.getDownloadUrl(downloadIndex);

			executorService.execute(new Runnable() {

				@Override
				public void run() {
					try {
						download(downloadIndex, url, handler);

						successCount.incrementAndGet();
						handler.onSuccess(downloadIndex, url);

					} catch (IOException ex) {
						handler.onError(downloadIndex, url, ex);

					} finally {
						if (countDown.decrementAndGet() == 0) {
							executorService.shutdown();
							handler.onAllDone(fromIndex, toIndex, successCount.get(), totalCount - successCount.get());
						}
					}
				}
			});
		}
	}

	public static abstract class Handler {

		public void initialize(final int index, final String url, final HttpClient client) {
			client.setResultReader(new ResponseReader() {

				@Override
				public Object read(InputStream is, Class<?> resultClass, HttpURLConnection conn) throws IOException {
					final String outPath = getOutputPath(index);
					OutputStream os = null;
					try {
						os = new FileOutputStream(outPath);
						IOUtils.copy(is, os);
					} finally {
						if (os != null) {
							os.close();
						}
					}
					return outPath;
				}
			});
			client.setResultClass(String.class);
		}

		public abstract int getPoolThreads();

		public abstract String getDownloadUrl(int index);

		public abstract String getOutputPath(int index);

		public void onError(int index, String url, IOException errorMessage) {
			System.err.println(String.format("[Downloader] onError: index=%d, url=%s, error=%s", index, url, errorMessage));
		}

		public void onSuccess(int index, String url) {
			System.out.println(String.format("[Downloader] onSuccess: index=%d, url=%s", index, url));
		}

		public void onAllDone(int fromIndex, int toIndex, int successCount, int errorCount) {
			System.out.println(String.format("[Downloader] onAllDone: fromIndex=%d, toIndex=%d, successCount=%d, errorCount=%d", fromIndex, toIndex, successCount, errorCount));
		}
	}
}
