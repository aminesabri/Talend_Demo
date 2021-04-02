package demotalend.bigfiles_0_1;

import routines.DataOperation;
import routines.TalendDataGenerator;
import routines.DataQuality;
import routines.Relational;
import routines.DataQualityDependencies;
import routines.Mathematical;
import routines.SQLike;
import routines.Numeric;
import routines.TalendStringUtil;
import routines.TalendString;
import routines.DQTechnical;
import routines.StringHandling;
import routines.DataMasking;
import routines.TalendDate;
import routines.DqStringHandling;
import routines.system.*;
import routines.system.api.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.math.BigDecimal;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.Comparator;

@SuppressWarnings("unused")
/**
 * Job: BigFiles Purpose: <br>
 * Description:  <br>
 * @author masabri@sqli.com
 * @version 7.1.1.20190228_0243-patch
 * @status 
 */
public class BigFiles implements TalendJob {
	static {
		System.setProperty("TalendJob.log", "BigFiles.log");
	}
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(BigFiles.class);

	protected static void logIgnoredError(String message, Throwable cause) {
		log.error(message, cause);

	}

	public final Object obj = new Object();

	// for transmiting parameters purpose
	private Object valueObject = null;

	public Object getValueObject() {
		return this.valueObject;
	}

	public void setValueObject(Object valueObject) {
		this.valueObject = valueObject;
	}

	private Object[] multiThreadLockWrite = new Object[0];

	private final static String defaultCharset = java.nio.charset.Charset
			.defaultCharset().name();

	private final static String utf8Charset = "UTF-8";

	// contains type for every context property
	public class PropertiesWithType extends java.util.Properties {
		private static final long serialVersionUID = 1L;
		private java.util.Map<String, String> propertyTypes = new java.util.HashMap<>();

		public PropertiesWithType(java.util.Properties properties) {
			super(properties);
		}

		public PropertiesWithType() {
			super();
		}

		public void setContextType(String key, String type) {
			propertyTypes.put(key, type);
		}

		public String getContextType(String key) {
			return propertyTypes.get(key);
		}
	}

	// create and load default properties
	private java.util.Properties defaultProps = new java.util.Properties();

	// create application properties with default
	public class ContextProperties extends PropertiesWithType {

		private static final long serialVersionUID = 1L;

		public ContextProperties(java.util.Properties properties) {
			super(properties);
		}

		public ContextProperties() {
			super();
		}

		public void synchronizeContext() {

		}

	}

	protected ContextProperties context = new ContextProperties(); // will be
																	// instanciated
																	// by MS.

	public ContextProperties getContext() {
		return this.context;
	}

	private final String jobVersion = "0.1";
	private final String jobName = "BigFiles";
	private final String projectName = "DEMOTALEND";
	public Integer errorCode = null;
	private String currentComponent = "";

	private final java.util.Map<String, Object> globalMap = java.util.Collections
			.synchronizedMap(new java.util.HashMap<String, Object>());

	private final java.util.Map<String, Long> start_Hash = java.util.Collections
			.synchronizedMap(new java.util.HashMap<String, Long>());
	private final java.util.Map<String, Long> end_Hash = java.util.Collections
			.synchronizedMap(new java.util.HashMap<String, Long>());
	private final java.util.Map<String, Boolean> ok_Hash = java.util.Collections
			.synchronizedMap(new java.util.HashMap<String, Boolean>());
	public final java.util.List<String[]> globalBuffer = java.util.Collections
			.synchronizedList(new java.util.ArrayList<String[]>());

	private RunStat runStat = new RunStat();

	// OSGi DataSource
	private final static String KEY_DB_DATASOURCES = "KEY_DB_DATASOURCES";

	private final static String KEY_DB_DATASOURCES_RAW = "KEY_DB_DATASOURCES_RAW";

	public void setDataSources(
			java.util.Map<String, javax.sql.DataSource> dataSources) {
		java.util.Map<String, routines.system.TalendDataSource> talendDataSources = new java.util.HashMap<String, routines.system.TalendDataSource>();
		for (java.util.Map.Entry<String, javax.sql.DataSource> dataSourceEntry : dataSources
				.entrySet()) {
			talendDataSources.put(
					dataSourceEntry.getKey(),
					new routines.system.TalendDataSource(dataSourceEntry
							.getValue()));
		}
		globalMap.put(KEY_DB_DATASOURCES, talendDataSources);
		globalMap
				.put(KEY_DB_DATASOURCES_RAW,
						new java.util.HashMap<String, javax.sql.DataSource>(
								dataSources));
	}

	LogCatcherUtils talendLogs_LOGS = new LogCatcherUtils();
	StatCatcherUtils talendStats_STATS = new StatCatcherUtils(
			"_76uKcJL9EeuMV9EnfKx5Qg", "0.1");
	MetterCatcherUtils talendMeter_METTER = new MetterCatcherUtils(
			"_76uKcJL9EeuMV9EnfKx5Qg", "0.1");

	private final java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
	private final java.io.PrintStream errorMessagePS = new java.io.PrintStream(
			new java.io.BufferedOutputStream(baos));

	public String getExceptionStackTrace() {
		if ("failure".equals(this.getStatus())) {
			errorMessagePS.flush();
			return baos.toString();
		}
		return null;
	}

	private Exception exception;

	public Exception getException() {
		if ("failure".equals(this.getStatus())) {
			return this.exception;
		}
		return null;
	}

	private class TalendException extends Exception {

		private static final long serialVersionUID = 1L;

		private java.util.Map<String, Object> globalMap = null;
		private Exception e = null;
		private String currentComponent = null;
		private String virtualComponentName = null;

		public void setVirtualComponentName(String virtualComponentName) {
			this.virtualComponentName = virtualComponentName;
		}

		private TalendException(Exception e, String errorComponent,
				final java.util.Map<String, Object> globalMap) {
			this.currentComponent = errorComponent;
			this.globalMap = globalMap;
			this.e = e;
		}

		public Exception getException() {
			return this.e;
		}

		public String getCurrentComponent() {
			return this.currentComponent;
		}

		public String getExceptionCauseMessage(Exception e) {
			Throwable cause = e;
			String message = null;
			int i = 10;
			while (null != cause && 0 < i--) {
				message = cause.getMessage();
				if (null == message) {
					cause = cause.getCause();
				} else {
					break;
				}
			}
			if (null == message) {
				message = e.getClass().getName();
			}
			return message;
		}

		@Override
		public void printStackTrace() {
			if (!(e instanceof TalendException || e instanceof TDieException)) {
				if (virtualComponentName != null
						&& currentComponent.indexOf(virtualComponentName + "_") == 0) {
					globalMap.put(virtualComponentName + "_ERROR_MESSAGE",
							getExceptionCauseMessage(e));
				}
				globalMap.put(currentComponent + "_ERROR_MESSAGE",
						getExceptionCauseMessage(e));
				System.err.println("Exception in component " + currentComponent
						+ " (" + jobName + ")");
			}
			if (!(e instanceof TDieException)) {
				if (e instanceof TalendException) {
					e.printStackTrace();
				} else {
					e.printStackTrace();
					e.printStackTrace(errorMessagePS);
					BigFiles.this.exception = e;
				}
			}
			if (!(e instanceof TalendException)) {
				try {
					for (java.lang.reflect.Method m : this.getClass()
							.getEnclosingClass().getMethods()) {
						if (m.getName().compareTo(currentComponent + "_error") == 0) {
							m.invoke(BigFiles.this, new Object[] { e,
									currentComponent, globalMap });
							break;
						}
					}

					if (!(e instanceof TDieException)) {
						talendLogs_LOGS.addMessage("Java Exception",
								currentComponent, 6, e.getClass().getName()
										+ ":" + e.getMessage(), 1);
						talendLogs_LOGSProcess(globalMap);
					}
				} catch (TalendException e) {
					// do nothing

				} catch (Exception e) {
					this.e.printStackTrace();
				}
			}
		}
	}

	public void tParallelize_1_error(Exception exception,
			String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		((java.util.Map) threadLocal.get()).put("status", "failure");

		tParallelize_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tRowGenerator_1_error(Exception exception,
			String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		talendStats_STATS.addMessage("failure", errorComponent,
				end_Hash.get(errorComponent) - start_Hash.get(errorComponent));
		talendStats_STATSProcess(globalMap);

		((java.util.Map) threadLocal.get()).put("status", "failure");

		tRowGenerator_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tFileOutputDelimited_1_error(Exception exception,
			String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		talendStats_STATS.addMessage("failure", errorComponent,
				end_Hash.get(errorComponent) - start_Hash.get(errorComponent));
		talendStats_STATSProcess(globalMap);

		((java.util.Map) threadLocal.get()).put("status", "failure");

		tRowGenerator_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tRowGenerator_2_error(Exception exception,
			String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		talendStats_STATS.addMessage("failure", errorComponent,
				end_Hash.get(errorComponent) - start_Hash.get(errorComponent));
		talendStats_STATSProcess(globalMap);

		((java.util.Map) threadLocal.get()).put("status", "failure");

		tRowGenerator_2_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tFileOutputDelimited_2_error(Exception exception,
			String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		talendStats_STATS.addMessage("failure", errorComponent,
				end_Hash.get(errorComponent) - start_Hash.get(errorComponent));
		talendStats_STATSProcess(globalMap);

		((java.util.Map) threadLocal.get()).put("status", "failure");

		tRowGenerator_2_onSubJobError(exception, errorComponent, globalMap);
	}

	public void talendStats_STATS_error(Exception exception,
			String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		talendStats_FILE_error(exception, errorComponent, globalMap);

	}

	public void talendStats_FILE_error(Exception exception,
			String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		((java.util.Map) threadLocal.get()).put("status", "failure");

		talendStats_STATS_onSubJobError(exception, errorComponent, globalMap);
	}

	public void talendLogs_LOGS_error(Exception exception,
			String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		talendLogs_FILE_error(exception, errorComponent, globalMap);

	}

	public void talendLogs_FILE_error(Exception exception,
			String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		((java.util.Map) threadLocal.get()).put("status", "failure");

		talendLogs_LOGS_onSubJobError(exception, errorComponent, globalMap);
	}

	public void talendMeter_METTER_error(Exception exception,
			String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		talendMeter_FILE_error(exception, errorComponent, globalMap);

	}

	public void talendMeter_FILE_error(Exception exception,
			String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		((java.util.Map) threadLocal.get()).put("status", "failure");

		talendMeter_METTER_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tParallelize_1_onSubJobError(Exception exception,
			String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread
				.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(),
				ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void tRowGenerator_1_onSubJobError(Exception exception,
			String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread
				.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(),
				ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void tRowGenerator_2_onSubJobError(Exception exception,
			String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread
				.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(),
				ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void talendStats_STATS_onSubJobError(Exception exception,
			String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread
				.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(),
				ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void talendLogs_LOGS_onSubJobError(Exception exception,
			String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread
				.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(),
				ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void talendMeter_METTER_onSubJobError(Exception exception,
			String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread
				.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(),
				ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void tParallelize_1Process(
			final java.util.Map<String, Object> globalMap)
			throws TalendException {
		globalMap.put("tParallelize_1_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		String iterateId = "";

		String currentComponent = "";
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception()
						.getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { // start the resume
				globalResumeTicket = true;

				/**
				 * [tParallelize_1 begin ] start
				 */

				ok_Hash.put("tParallelize_1", false);
				start_Hash.put("tParallelize_1", System.currentTimeMillis());

				currentComponent = "tParallelize_1";

				int tos_count_tParallelize_1 = 0;

				// call parallelized subjobs
				java.util.Map<String, Thread> mt_tParallelize_1 = new java.util.HashMap<String, Thread>();

				// clear the temporary values in the globalMap
				globalMap.remove("tRowGenerator_1_SUBPROCESS_STATE");
				globalMap.remove("tRowGenerator_2_SUBPROCESS_STATE");

				java.util.Map concurrentMap_temp_tParallelize_1;
				if (globalMap instanceof java.util.HashMap) {
					concurrentMap_temp_tParallelize_1 = java.util.Collections
							.synchronizedMap(globalMap);
				} else {
					concurrentMap_temp_tParallelize_1 = globalMap;
				}
				final java.util.Map concurrentMap_tParallelize_1 = concurrentMap_temp_tParallelize_1;

				runningThreadCount.add(1);
				String name_tRowGenerator_1 = "tParallelize_1_"
						+ java.util.UUID.randomUUID().toString();
				Thread thread_tRowGenerator_1 = new Thread(new ThreadGroup(
						name_tRowGenerator_1), name_tRowGenerator_1) {
					public void run() {
						java.util.Map threadRunResultMap = new java.util.HashMap();
						threadRunResultMap.put("errorCode", null);
						threadRunResultMap.put("status", "");
						threadLocal.set(threadRunResultMap);

						try {

							log.debug("tParallelize_1 - The subjob starting with the component 'tRowGenerator_1' starts.");

							tRowGenerator_1Process(concurrentMap_tParallelize_1);

							log.debug("tParallelize_1 - The subjob starting with the component 'tRowGenerator_1' is done.");

						} catch (TalendException e) {

							log.error("tParallelize_1 - " + e.getMessage());

							concurrentMap_tParallelize_1.put(
									"tRowGenerator_1_SUBPROCESS_STATE", -1);
							e.printStackTrace();
						} catch (java.lang.Error error) {

							log.error("tParallelize_1 - " + error.getMessage());

							concurrentMap_tParallelize_1.put(
									"tRowGenerator_1_SUBPROCESS_STATE", -1);
							error.printStackTrace();
						} finally {
							runningThreadCount.add(-1);
						}
					}
				};
				thread_tRowGenerator_1.start();
				mt_tParallelize_1
						.put("tRowGenerator_1", thread_tRowGenerator_1);
				runningThreadCount.add(1);
				String name_tRowGenerator_2 = "tParallelize_1_"
						+ java.util.UUID.randomUUID().toString();
				Thread thread_tRowGenerator_2 = new Thread(new ThreadGroup(
						name_tRowGenerator_2), name_tRowGenerator_2) {
					public void run() {
						java.util.Map threadRunResultMap = new java.util.HashMap();
						threadRunResultMap.put("errorCode", null);
						threadRunResultMap.put("status", "");
						threadLocal.set(threadRunResultMap);

						try {

							log.debug("tParallelize_1 - The subjob starting with the component 'tRowGenerator_2' starts.");

							tRowGenerator_2Process(concurrentMap_tParallelize_1);

							log.debug("tParallelize_1 - The subjob starting with the component 'tRowGenerator_2' is done.");

						} catch (TalendException e) {

							log.error("tParallelize_1 - " + e.getMessage());

							concurrentMap_tParallelize_1.put(
									"tRowGenerator_2_SUBPROCESS_STATE", -1);
							e.printStackTrace();
						} catch (java.lang.Error error) {

							log.error("tParallelize_1 - " + error.getMessage());

							concurrentMap_tParallelize_1.put(
									"tRowGenerator_2_SUBPROCESS_STATE", -1);
							error.printStackTrace();
						} finally {
							runningThreadCount.add(-1);
						}
					}
				};
				thread_tRowGenerator_2.start();
				mt_tParallelize_1
						.put("tRowGenerator_2", thread_tRowGenerator_2);
				while ((((globalMap.get("tRowGenerator_1_SUBPROCESS_STATE") == null) ? true
						: ((Integer) globalMap
								.get("tRowGenerator_1_SUBPROCESS_STATE") == 0))
						|| ((globalMap.get("tRowGenerator_2_SUBPROCESS_STATE") == null) ? true
								: ((Integer) globalMap
										.get("tRowGenerator_2_SUBPROCESS_STATE") == 0)) || false)) {
					Thread.sleep(100);
				}

				// call next subprocesses

				/**
				 * [tParallelize_1 begin ] stop
				 */

				/**
				 * [tParallelize_1 main ] start
				 */

				currentComponent = "tParallelize_1";

				tos_count_tParallelize_1++;

				/**
				 * [tParallelize_1 main ] stop
				 */

				/**
				 * [tParallelize_1 process_data_begin ] start
				 */

				currentComponent = "tParallelize_1";

				/**
				 * [tParallelize_1 process_data_begin ] stop
				 */

				/**
				 * [tParallelize_1 process_data_end ] start
				 */

				currentComponent = "tParallelize_1";

				/**
				 * [tParallelize_1 process_data_end ] stop
				 */

				/**
				 * [tParallelize_1 end ] start
				 */

				currentComponent = "tParallelize_1";

				ok_Hash.put("tParallelize_1", true);
				end_Hash.put("tParallelize_1", System.currentTimeMillis());

				/**
				 * [tParallelize_1 end ] stop
				 */
			}// end the resume

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent,
					globalMap);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [tParallelize_1 finally ] start
				 */

				currentComponent = "tParallelize_1";

				/**
				 * [tParallelize_1 finally ] stop
				 */
			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tParallelize_1_SUBPROCESS_STATE", 1);
	}

	public static class row1Struct implements
			routines.system.IPersistableRow<row1Struct> {
		final static byte[] commonByteArrayLock_DEMOTALEND_BigFiles = new byte[0];
		static byte[] commonByteArray_DEMOTALEND_BigFiles = new byte[0];

		public String Prenom;

		public String getPrenom() {
			return this.Prenom;
		}

		public String Nom;

		public String getNom() {
			return this.Nom;
		}

		public String Ville;

		public String getVille() {
			return this.Ville;
		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_DEMOTALEND_BigFiles.length) {
					if (length < 1024
							&& commonByteArray_DEMOTALEND_BigFiles.length == 0) {
						commonByteArray_DEMOTALEND_BigFiles = new byte[1024];
					} else {
						commonByteArray_DEMOTALEND_BigFiles = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_DEMOTALEND_BigFiles, 0, length);
				strReturn = new String(commonByteArray_DEMOTALEND_BigFiles, 0,
						length, utf8Charset);
			}
			return strReturn;
		}

		private void writeString(String str, ObjectOutputStream dos)
				throws IOException {
			if (str == null) {
				dos.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				dos.writeInt(byteArray.length);
				dos.write(byteArray);
			}
		}

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_DEMOTALEND_BigFiles) {

				try {

					int length = 0;

					this.Prenom = readString(dis);

					this.Nom = readString(dis);

					this.Ville = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// String

				writeString(this.Prenom, dos);

				// String

				writeString(this.Nom, dos);

				// String

				writeString(this.Ville, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("Prenom=" + Prenom);
			sb.append(",Nom=" + Nom);
			sb.append(",Ville=" + Ville);
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (Prenom == null) {
				sb.append("<null>");
			} else {
				sb.append(Prenom);
			}

			sb.append("|");

			if (Nom == null) {
				sb.append("<null>");
			} else {
				sb.append(Nom);
			}

			sb.append("|");

			if (Ville == null) {
				sb.append("<null>");
			} else {
				sb.append(Ville);
			}

			sb.append("|");

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(row1Struct other) {

			int returnValue = -1;

			return returnValue;
		}

		private int checkNullsAndCompare(Object object1, Object object2) {
			int returnValue = 0;
			if (object1 instanceof Comparable && object2 instanceof Comparable) {
				returnValue = ((Comparable) object1).compareTo(object2);
			} else if (object1 != null && object2 != null) {
				returnValue = compareStrings(object1.toString(),
						object2.toString());
			} else if (object1 == null && object2 != null) {
				returnValue = 1;
			} else if (object1 != null && object2 == null) {
				returnValue = -1;
			} else {
				returnValue = 0;
			}

			return returnValue;
		}

		private int compareStrings(String string1, String string2) {
			return string1.compareTo(string2);
		}

	}

	public void tRowGenerator_1Process(
			final java.util.Map<String, Object> globalMap)
			throws TalendException {
		globalMap.put("tRowGenerator_1_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		String iterateId = "";

		String currentComponent = "";
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception()
						.getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { // start the resume
				globalResumeTicket = true;

				row1Struct row1 = new row1Struct();

				/**
				 * [tFileOutputDelimited_1 begin ] start
				 */

				ok_Hash.put("tFileOutputDelimited_1", false);
				start_Hash.put("tFileOutputDelimited_1",
						System.currentTimeMillis());

				talendStats_STATS.addMessage("begin", "tFileOutputDelimited_1");
				talendStats_STATSProcess(globalMap);

				currentComponent = "tFileOutputDelimited_1";

				if (execStat) {
					if (resourceMap.get("inIterateVComp") == null) {

						runStat.updateStatOnConnection("row1" + iterateId, 0, 0);

					}
				}

				int tos_count_tFileOutputDelimited_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tFileOutputDelimited_1 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tFileOutputDelimited_1 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tFileOutputDelimited_1 = new StringBuilder();
							log4jParamters_tFileOutputDelimited_1
									.append("Parameters:");
							log4jParamters_tFileOutputDelimited_1
									.append("USESTREAM" + " = " + "false");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1
									.append("FILENAME"
											+ " = "
											+ "\"C:/Users/masabri/Desktop/clientsGE.csv\"");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1
									.append("ROWSEPARATOR" + " = " + "\"\\n\"");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1
									.append("FIELDSEPARATOR" + " = " + "\";\"");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1
									.append("APPEND" + " = " + "false");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1
									.append("INCLUDEHEADER" + " = " + "true");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1
									.append("COMPRESS" + " = " + "false");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1
									.append("ADVANCED_SEPARATOR" + " = "
											+ "false");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1
									.append("CSV_OPTION" + " = " + "false");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1
									.append("CREATE" + " = " + "true");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1
									.append("SPLIT" + " = " + "false");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1
									.append("FLUSHONROW" + " = " + "false");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1
									.append("ROW_MODE" + " = " + "false");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1
									.append("ENCODING" + " = "
											+ "\"ISO-8859-15\"");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1
									.append("DELETE_EMPTYFILE" + " = "
											+ "false");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1
									.append("FILE_EXIST_EXCEPTION" + " = "
											+ "false");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tFileOutputDelimited_1 - "
										+ (log4jParamters_tFileOutputDelimited_1));
						}
					}
					new BytesLimit65535_tFileOutputDelimited_1()
							.limitLog4jByte();
				}

				String fileName_tFileOutputDelimited_1 = "";
				fileName_tFileOutputDelimited_1 = (new java.io.File(
						"C:/Users/masabri/Desktop/clientsGE.csv"))
						.getAbsolutePath().replace("\\", "/");
				String fullName_tFileOutputDelimited_1 = null;
				String extension_tFileOutputDelimited_1 = null;
				String directory_tFileOutputDelimited_1 = null;
				if ((fileName_tFileOutputDelimited_1.indexOf("/") != -1)) {
					if (fileName_tFileOutputDelimited_1.lastIndexOf(".") < fileName_tFileOutputDelimited_1
							.lastIndexOf("/")) {
						fullName_tFileOutputDelimited_1 = fileName_tFileOutputDelimited_1;
						extension_tFileOutputDelimited_1 = "";
					} else {
						fullName_tFileOutputDelimited_1 = fileName_tFileOutputDelimited_1
								.substring(0, fileName_tFileOutputDelimited_1
										.lastIndexOf("."));
						extension_tFileOutputDelimited_1 = fileName_tFileOutputDelimited_1
								.substring(fileName_tFileOutputDelimited_1
										.lastIndexOf("."));
					}
					directory_tFileOutputDelimited_1 = fileName_tFileOutputDelimited_1
							.substring(0, fileName_tFileOutputDelimited_1
									.lastIndexOf("/"));
				} else {
					if (fileName_tFileOutputDelimited_1.lastIndexOf(".") != -1) {
						fullName_tFileOutputDelimited_1 = fileName_tFileOutputDelimited_1
								.substring(0, fileName_tFileOutputDelimited_1
										.lastIndexOf("."));
						extension_tFileOutputDelimited_1 = fileName_tFileOutputDelimited_1
								.substring(fileName_tFileOutputDelimited_1
										.lastIndexOf("."));
					} else {
						fullName_tFileOutputDelimited_1 = fileName_tFileOutputDelimited_1;
						extension_tFileOutputDelimited_1 = "";
					}
					directory_tFileOutputDelimited_1 = "";
				}
				boolean isFileGenerated_tFileOutputDelimited_1 = true;
				java.io.File filetFileOutputDelimited_1 = new java.io.File(
						fileName_tFileOutputDelimited_1);
				globalMap.put("tFileOutputDelimited_1_FILE_NAME",
						fileName_tFileOutputDelimited_1);
				int nb_line_tFileOutputDelimited_1 = 0;
				int splitedFileNo_tFileOutputDelimited_1 = 0;
				int currentRow_tFileOutputDelimited_1 = 0;

				final String OUT_DELIM_tFileOutputDelimited_1 = /**
				 * Start field
				 * tFileOutputDelimited_1:FIELDSEPARATOR
				 */
				";"/** End field tFileOutputDelimited_1:FIELDSEPARATOR */
				;

				final String OUT_DELIM_ROWSEP_tFileOutputDelimited_1 = /**
				 * Start
				 * field tFileOutputDelimited_1:ROWSEPARATOR
				 */
				"\n"/** End field tFileOutputDelimited_1:ROWSEPARATOR */
				;

				// create directory only if not exists
				if (directory_tFileOutputDelimited_1 != null
						&& directory_tFileOutputDelimited_1.trim().length() != 0) {
					java.io.File dir_tFileOutputDelimited_1 = new java.io.File(
							directory_tFileOutputDelimited_1);
					if (!dir_tFileOutputDelimited_1.exists()) {
						log.info("tFileOutputDelimited_1 - Creating directory '"
								+ dir_tFileOutputDelimited_1.getCanonicalPath()
								+ "'.");
						dir_tFileOutputDelimited_1.mkdirs();
						log.info("tFileOutputDelimited_1 - The directory '"
								+ dir_tFileOutputDelimited_1.getCanonicalPath()
								+ "' has been created successfully.");
					}
				}

				// routines.system.Row
				java.io.Writer outtFileOutputDelimited_1 = null;

				java.io.File fileToDelete_tFileOutputDelimited_1 = new java.io.File(
						fileName_tFileOutputDelimited_1);
				if (fileToDelete_tFileOutputDelimited_1.exists()) {
					fileToDelete_tFileOutputDelimited_1.delete();
				}
				outtFileOutputDelimited_1 = new java.io.BufferedWriter(
						new java.io.OutputStreamWriter(
								new java.io.FileOutputStream(
										fileName_tFileOutputDelimited_1, false),
								"ISO-8859-15"));
				synchronized (multiThreadLockWrite) {
					if (filetFileOutputDelimited_1.length() == 0) {
						outtFileOutputDelimited_1.write("Prenom");
						outtFileOutputDelimited_1
								.write(OUT_DELIM_tFileOutputDelimited_1);
						outtFileOutputDelimited_1.write("Nom");
						outtFileOutputDelimited_1
								.write(OUT_DELIM_tFileOutputDelimited_1);
						outtFileOutputDelimited_1.write("Ville");
						outtFileOutputDelimited_1
								.write(OUT_DELIM_ROWSEP_tFileOutputDelimited_1);
						outtFileOutputDelimited_1.flush();
					}
				}

				resourceMap.put("out_tFileOutputDelimited_1",
						outtFileOutputDelimited_1);
				resourceMap.put("nb_line_tFileOutputDelimited_1",
						nb_line_tFileOutputDelimited_1);

				/**
				 * [tFileOutputDelimited_1 begin ] stop
				 */

				/**
				 * [tRowGenerator_1 begin ] start
				 */

				ok_Hash.put("tRowGenerator_1", false);
				start_Hash.put("tRowGenerator_1", System.currentTimeMillis());

				talendStats_STATS.addMessage("begin", "tRowGenerator_1");
				talendStats_STATSProcess(globalMap);

				currentComponent = "tRowGenerator_1";

				int tos_count_tRowGenerator_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tRowGenerator_1 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tRowGenerator_1 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tRowGenerator_1 = new StringBuilder();
							log4jParamters_tRowGenerator_1
									.append("Parameters:");
							if (log.isDebugEnabled())
								log.debug("tRowGenerator_1 - "
										+ (log4jParamters_tRowGenerator_1));
						}
					}
					new BytesLimit65535_tRowGenerator_1().limitLog4jByte();
				}

				int nb_line_tRowGenerator_1 = 0;
				int nb_max_row_tRowGenerator_1 = 20000000;

				class tRowGenerator_1Randomizer {
					public String getRandomPrenom() {

						return TalendDataGenerator.getFirstName();

					}

					public String getRandomNom() {

						return TalendDataGenerator.getLastName();

					}

					public String getRandomVille() {

						return TalendDataGenerator.getUsCity();

					}
				}
				tRowGenerator_1Randomizer randtRowGenerator_1 = new tRowGenerator_1Randomizer();

				log.info("tRowGenerator_1 - Generating records.");
				for (int itRowGenerator_1 = 0; itRowGenerator_1 < nb_max_row_tRowGenerator_1; itRowGenerator_1++) {
					row1.Prenom = randtRowGenerator_1.getRandomPrenom();
					row1.Nom = randtRowGenerator_1.getRandomNom();
					row1.Ville = randtRowGenerator_1.getRandomVille();
					nb_line_tRowGenerator_1++;

					log.debug("tRowGenerator_1 - Retrieving the record "
							+ nb_line_tRowGenerator_1 + ".");

					/**
					 * [tRowGenerator_1 begin ] stop
					 */

					/**
					 * [tRowGenerator_1 main ] start
					 */

					currentComponent = "tRowGenerator_1";

					tos_count_tRowGenerator_1++;

					/**
					 * [tRowGenerator_1 main ] stop
					 */

					/**
					 * [tRowGenerator_1 process_data_begin ] start
					 */

					currentComponent = "tRowGenerator_1";

					/**
					 * [tRowGenerator_1 process_data_begin ] stop
					 */

					/**
					 * [tFileOutputDelimited_1 main ] start
					 */

					currentComponent = "tFileOutputDelimited_1";

					// row1
					// row1

					if (execStat) {
						runStat.updateStatOnConnection("row1" + iterateId, 1, 1);
					}

					if (log.isTraceEnabled()) {
						log.trace("row1 - "
								+ (row1 == null ? "" : row1.toLogString()));
					}

					StringBuilder sb_tFileOutputDelimited_1 = new StringBuilder();
					if (row1.Prenom != null) {
						sb_tFileOutputDelimited_1.append(row1.Prenom);
					}
					sb_tFileOutputDelimited_1
							.append(OUT_DELIM_tFileOutputDelimited_1);
					if (row1.Nom != null) {
						sb_tFileOutputDelimited_1.append(row1.Nom);
					}
					sb_tFileOutputDelimited_1
							.append(OUT_DELIM_tFileOutputDelimited_1);
					if (row1.Ville != null) {
						sb_tFileOutputDelimited_1.append(row1.Ville);
					}
					sb_tFileOutputDelimited_1
							.append(OUT_DELIM_ROWSEP_tFileOutputDelimited_1);

					synchronized (multiThreadLockWrite) {
						nb_line_tFileOutputDelimited_1++;
						resourceMap.put("nb_line_tFileOutputDelimited_1",
								nb_line_tFileOutputDelimited_1);

						outtFileOutputDelimited_1
								.write(sb_tFileOutputDelimited_1.toString());
						log.debug("tFileOutputDelimited_1 - Writing the record "
								+ nb_line_tFileOutputDelimited_1 + ".");

					}

					tos_count_tFileOutputDelimited_1++;

					/**
					 * [tFileOutputDelimited_1 main ] stop
					 */

					/**
					 * [tFileOutputDelimited_1 process_data_begin ] start
					 */

					currentComponent = "tFileOutputDelimited_1";

					/**
					 * [tFileOutputDelimited_1 process_data_begin ] stop
					 */

					/**
					 * [tFileOutputDelimited_1 process_data_end ] start
					 */

					currentComponent = "tFileOutputDelimited_1";

					/**
					 * [tFileOutputDelimited_1 process_data_end ] stop
					 */

					/**
					 * [tRowGenerator_1 process_data_end ] start
					 */

					currentComponent = "tRowGenerator_1";

					/**
					 * [tRowGenerator_1 process_data_end ] stop
					 */

					/**
					 * [tRowGenerator_1 end ] start
					 */

					currentComponent = "tRowGenerator_1";

				}
				globalMap.put("tRowGenerator_1_NB_LINE",
						nb_line_tRowGenerator_1);
				log.info("tRowGenerator_1 - Generated records count:"
						+ nb_line_tRowGenerator_1 + " .");

				if (log.isDebugEnabled())
					log.debug("tRowGenerator_1 - " + ("Done."));

				ok_Hash.put("tRowGenerator_1", true);
				end_Hash.put("tRowGenerator_1", System.currentTimeMillis());

				talendStats_STATS.addMessage(
						"end",
						"tRowGenerator_1",
						end_Hash.get("tRowGenerator_1")
								- start_Hash.get("tRowGenerator_1"));
				talendStats_STATSProcess(globalMap);

				/**
				 * [tRowGenerator_1 end ] stop
				 */

				/**
				 * [tFileOutputDelimited_1 end ] start
				 */

				currentComponent = "tFileOutputDelimited_1";

				synchronized (multiThreadLockWrite) {

					if (outtFileOutputDelimited_1 != null) {
						outtFileOutputDelimited_1.flush();
						outtFileOutputDelimited_1.close();
					}

					globalMap.put("tFileOutputDelimited_1_NB_LINE",
							nb_line_tFileOutputDelimited_1);
					globalMap.put("tFileOutputDelimited_1_FILE_NAME",
							fileName_tFileOutputDelimited_1);

				}

				resourceMap.put("finish_tFileOutputDelimited_1", true);

				log.debug("tFileOutputDelimited_1 - Written records count: "
						+ nb_line_tFileOutputDelimited_1 + " .");

				if (execStat) {
					if (resourceMap.get("inIterateVComp") == null
							|| !((Boolean) resourceMap.get("inIterateVComp"))) {
						runStat.updateStatOnConnection("row1" + iterateId, 2, 0);
					}
				}

				if (log.isDebugEnabled())
					log.debug("tFileOutputDelimited_1 - " + ("Done."));

				ok_Hash.put("tFileOutputDelimited_1", true);
				end_Hash.put("tFileOutputDelimited_1",
						System.currentTimeMillis());

				talendStats_STATS.addMessage(
						"end",
						"tFileOutputDelimited_1",
						end_Hash.get("tFileOutputDelimited_1")
								- start_Hash.get("tFileOutputDelimited_1"));
				talendStats_STATSProcess(globalMap);

				/**
				 * [tFileOutputDelimited_1 end ] stop
				 */

			}// end the resume

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent,
					globalMap);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [tRowGenerator_1 finally ] start
				 */

				currentComponent = "tRowGenerator_1";

				/**
				 * [tRowGenerator_1 finally ] stop
				 */

				/**
				 * [tFileOutputDelimited_1 finally ] start
				 */

				currentComponent = "tFileOutputDelimited_1";

				if (resourceMap.get("finish_tFileOutputDelimited_1") == null) {

					synchronized (multiThreadLockWrite) {

						java.io.Writer outtFileOutputDelimited_1 = (java.io.Writer) resourceMap
								.get("out_tFileOutputDelimited_1");
						if (outtFileOutputDelimited_1 != null) {
							outtFileOutputDelimited_1.flush();
							outtFileOutputDelimited_1.close();
						}

					}

				}

				/**
				 * [tFileOutputDelimited_1 finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tRowGenerator_1_SUBPROCESS_STATE", 1);
	}

	public static class row2Struct implements
			routines.system.IPersistableRow<row2Struct> {
		final static byte[] commonByteArrayLock_DEMOTALEND_BigFiles = new byte[0];
		static byte[] commonByteArray_DEMOTALEND_BigFiles = new byte[0];

		public String dummy;

		public String getDummy() {
			return this.dummy;
		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_DEMOTALEND_BigFiles.length) {
					if (length < 1024
							&& commonByteArray_DEMOTALEND_BigFiles.length == 0) {
						commonByteArray_DEMOTALEND_BigFiles = new byte[1024];
					} else {
						commonByteArray_DEMOTALEND_BigFiles = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_DEMOTALEND_BigFiles, 0, length);
				strReturn = new String(commonByteArray_DEMOTALEND_BigFiles, 0,
						length, utf8Charset);
			}
			return strReturn;
		}

		private void writeString(String str, ObjectOutputStream dos)
				throws IOException {
			if (str == null) {
				dos.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				dos.writeInt(byteArray.length);
				dos.write(byteArray);
			}
		}

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_DEMOTALEND_BigFiles) {

				try {

					int length = 0;

					this.dummy = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// String

				writeString(this.dummy, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("dummy=" + dummy);
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (dummy == null) {
				sb.append("<null>");
			} else {
				sb.append(dummy);
			}

			sb.append("|");

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(row2Struct other) {

			int returnValue = -1;

			return returnValue;
		}

		private int checkNullsAndCompare(Object object1, Object object2) {
			int returnValue = 0;
			if (object1 instanceof Comparable && object2 instanceof Comparable) {
				returnValue = ((Comparable) object1).compareTo(object2);
			} else if (object1 != null && object2 != null) {
				returnValue = compareStrings(object1.toString(),
						object2.toString());
			} else if (object1 == null && object2 != null) {
				returnValue = 1;
			} else if (object1 != null && object2 == null) {
				returnValue = -1;
			} else {
				returnValue = 0;
			}

			return returnValue;
		}

		private int compareStrings(String string1, String string2) {
			return string1.compareTo(string2);
		}

	}

	public void tRowGenerator_2Process(
			final java.util.Map<String, Object> globalMap)
			throws TalendException {
		globalMap.put("tRowGenerator_2_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		String iterateId = "";

		String currentComponent = "";
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception()
						.getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { // start the resume
				globalResumeTicket = true;

				row2Struct row2 = new row2Struct();

				/**
				 * [tFileOutputDelimited_2 begin ] start
				 */

				ok_Hash.put("tFileOutputDelimited_2", false);
				start_Hash.put("tFileOutputDelimited_2",
						System.currentTimeMillis());

				talendStats_STATS.addMessage("begin", "tFileOutputDelimited_2");
				talendStats_STATSProcess(globalMap);

				currentComponent = "tFileOutputDelimited_2";

				if (execStat) {
					if (resourceMap.get("inIterateVComp") == null) {

						runStat.updateStatOnConnection("row2" + iterateId, 0, 0);

					}
				}

				int tos_count_tFileOutputDelimited_2 = 0;

				if (log.isDebugEnabled())
					log.debug("tFileOutputDelimited_2 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tFileOutputDelimited_2 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tFileOutputDelimited_2 = new StringBuilder();
							log4jParamters_tFileOutputDelimited_2
									.append("Parameters:");
							log4jParamters_tFileOutputDelimited_2
									.append("USESTREAM" + " = " + "false");
							log4jParamters_tFileOutputDelimited_2.append(" | ");
							log4jParamters_tFileOutputDelimited_2
									.append("FILENAME"
											+ " = "
											+ "\"C:/Users/masabri/Desktop/dummy.csv\"");
							log4jParamters_tFileOutputDelimited_2.append(" | ");
							log4jParamters_tFileOutputDelimited_2
									.append("ROWSEPARATOR" + " = " + "\"\\n\"");
							log4jParamters_tFileOutputDelimited_2.append(" | ");
							log4jParamters_tFileOutputDelimited_2
									.append("FIELDSEPARATOR" + " = " + "\";\"");
							log4jParamters_tFileOutputDelimited_2.append(" | ");
							log4jParamters_tFileOutputDelimited_2
									.append("APPEND" + " = " + "false");
							log4jParamters_tFileOutputDelimited_2.append(" | ");
							log4jParamters_tFileOutputDelimited_2
									.append("INCLUDEHEADER" + " = " + "false");
							log4jParamters_tFileOutputDelimited_2.append(" | ");
							log4jParamters_tFileOutputDelimited_2
									.append("COMPRESS" + " = " + "false");
							log4jParamters_tFileOutputDelimited_2.append(" | ");
							log4jParamters_tFileOutputDelimited_2
									.append("ADVANCED_SEPARATOR" + " = "
											+ "false");
							log4jParamters_tFileOutputDelimited_2.append(" | ");
							log4jParamters_tFileOutputDelimited_2
									.append("CSV_OPTION" + " = " + "false");
							log4jParamters_tFileOutputDelimited_2.append(" | ");
							log4jParamters_tFileOutputDelimited_2
									.append("CREATE" + " = " + "true");
							log4jParamters_tFileOutputDelimited_2.append(" | ");
							log4jParamters_tFileOutputDelimited_2
									.append("SPLIT" + " = " + "false");
							log4jParamters_tFileOutputDelimited_2.append(" | ");
							log4jParamters_tFileOutputDelimited_2
									.append("FLUSHONROW" + " = " + "false");
							log4jParamters_tFileOutputDelimited_2.append(" | ");
							log4jParamters_tFileOutputDelimited_2
									.append("ROW_MODE" + " = " + "false");
							log4jParamters_tFileOutputDelimited_2.append(" | ");
							log4jParamters_tFileOutputDelimited_2
									.append("ENCODING" + " = "
											+ "\"ISO-8859-15\"");
							log4jParamters_tFileOutputDelimited_2.append(" | ");
							log4jParamters_tFileOutputDelimited_2
									.append("DELETE_EMPTYFILE" + " = "
											+ "false");
							log4jParamters_tFileOutputDelimited_2.append(" | ");
							log4jParamters_tFileOutputDelimited_2
									.append("FILE_EXIST_EXCEPTION" + " = "
											+ "false");
							log4jParamters_tFileOutputDelimited_2.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tFileOutputDelimited_2 - "
										+ (log4jParamters_tFileOutputDelimited_2));
						}
					}
					new BytesLimit65535_tFileOutputDelimited_2()
							.limitLog4jByte();
				}

				String fileName_tFileOutputDelimited_2 = "";
				fileName_tFileOutputDelimited_2 = (new java.io.File(
						"C:/Users/masabri/Desktop/dummy.csv"))
						.getAbsolutePath().replace("\\", "/");
				String fullName_tFileOutputDelimited_2 = null;
				String extension_tFileOutputDelimited_2 = null;
				String directory_tFileOutputDelimited_2 = null;
				if ((fileName_tFileOutputDelimited_2.indexOf("/") != -1)) {
					if (fileName_tFileOutputDelimited_2.lastIndexOf(".") < fileName_tFileOutputDelimited_2
							.lastIndexOf("/")) {
						fullName_tFileOutputDelimited_2 = fileName_tFileOutputDelimited_2;
						extension_tFileOutputDelimited_2 = "";
					} else {
						fullName_tFileOutputDelimited_2 = fileName_tFileOutputDelimited_2
								.substring(0, fileName_tFileOutputDelimited_2
										.lastIndexOf("."));
						extension_tFileOutputDelimited_2 = fileName_tFileOutputDelimited_2
								.substring(fileName_tFileOutputDelimited_2
										.lastIndexOf("."));
					}
					directory_tFileOutputDelimited_2 = fileName_tFileOutputDelimited_2
							.substring(0, fileName_tFileOutputDelimited_2
									.lastIndexOf("/"));
				} else {
					if (fileName_tFileOutputDelimited_2.lastIndexOf(".") != -1) {
						fullName_tFileOutputDelimited_2 = fileName_tFileOutputDelimited_2
								.substring(0, fileName_tFileOutputDelimited_2
										.lastIndexOf("."));
						extension_tFileOutputDelimited_2 = fileName_tFileOutputDelimited_2
								.substring(fileName_tFileOutputDelimited_2
										.lastIndexOf("."));
					} else {
						fullName_tFileOutputDelimited_2 = fileName_tFileOutputDelimited_2;
						extension_tFileOutputDelimited_2 = "";
					}
					directory_tFileOutputDelimited_2 = "";
				}
				boolean isFileGenerated_tFileOutputDelimited_2 = true;
				java.io.File filetFileOutputDelimited_2 = new java.io.File(
						fileName_tFileOutputDelimited_2);
				globalMap.put("tFileOutputDelimited_2_FILE_NAME",
						fileName_tFileOutputDelimited_2);
				int nb_line_tFileOutputDelimited_2 = 0;
				int splitedFileNo_tFileOutputDelimited_2 = 0;
				int currentRow_tFileOutputDelimited_2 = 0;

				final String OUT_DELIM_tFileOutputDelimited_2 = /**
				 * Start field
				 * tFileOutputDelimited_2:FIELDSEPARATOR
				 */
				";"/** End field tFileOutputDelimited_2:FIELDSEPARATOR */
				;

				final String OUT_DELIM_ROWSEP_tFileOutputDelimited_2 = /**
				 * Start
				 * field tFileOutputDelimited_2:ROWSEPARATOR
				 */
				"\n"/** End field tFileOutputDelimited_2:ROWSEPARATOR */
				;

				// create directory only if not exists
				if (directory_tFileOutputDelimited_2 != null
						&& directory_tFileOutputDelimited_2.trim().length() != 0) {
					java.io.File dir_tFileOutputDelimited_2 = new java.io.File(
							directory_tFileOutputDelimited_2);
					if (!dir_tFileOutputDelimited_2.exists()) {
						log.info("tFileOutputDelimited_2 - Creating directory '"
								+ dir_tFileOutputDelimited_2.getCanonicalPath()
								+ "'.");
						dir_tFileOutputDelimited_2.mkdirs();
						log.info("tFileOutputDelimited_2 - The directory '"
								+ dir_tFileOutputDelimited_2.getCanonicalPath()
								+ "' has been created successfully.");
					}
				}

				// routines.system.Row
				java.io.Writer outtFileOutputDelimited_2 = null;

				java.io.File fileToDelete_tFileOutputDelimited_2 = new java.io.File(
						fileName_tFileOutputDelimited_2);
				if (fileToDelete_tFileOutputDelimited_2.exists()) {
					fileToDelete_tFileOutputDelimited_2.delete();
				}
				outtFileOutputDelimited_2 = new java.io.BufferedWriter(
						new java.io.OutputStreamWriter(
								new java.io.FileOutputStream(
										fileName_tFileOutputDelimited_2, false),
								"ISO-8859-15"));

				resourceMap.put("out_tFileOutputDelimited_2",
						outtFileOutputDelimited_2);
				resourceMap.put("nb_line_tFileOutputDelimited_2",
						nb_line_tFileOutputDelimited_2);

				/**
				 * [tFileOutputDelimited_2 begin ] stop
				 */

				/**
				 * [tRowGenerator_2 begin ] start
				 */

				ok_Hash.put("tRowGenerator_2", false);
				start_Hash.put("tRowGenerator_2", System.currentTimeMillis());

				talendStats_STATS.addMessage("begin", "tRowGenerator_2");
				talendStats_STATSProcess(globalMap);

				currentComponent = "tRowGenerator_2";

				int tos_count_tRowGenerator_2 = 0;

				if (log.isDebugEnabled())
					log.debug("tRowGenerator_2 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tRowGenerator_2 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tRowGenerator_2 = new StringBuilder();
							log4jParamters_tRowGenerator_2
									.append("Parameters:");
							if (log.isDebugEnabled())
								log.debug("tRowGenerator_2 - "
										+ (log4jParamters_tRowGenerator_2));
						}
					}
					new BytesLimit65535_tRowGenerator_2().limitLog4jByte();
				}

				int nb_line_tRowGenerator_2 = 0;
				int nb_max_row_tRowGenerator_2 = 15000000;

				class tRowGenerator_2Randomizer {
					public String getRandomdummy() {

						return TalendString.getAsciiRandomString(6);

					}
				}
				tRowGenerator_2Randomizer randtRowGenerator_2 = new tRowGenerator_2Randomizer();

				log.info("tRowGenerator_2 - Generating records.");
				for (int itRowGenerator_2 = 0; itRowGenerator_2 < nb_max_row_tRowGenerator_2; itRowGenerator_2++) {
					row2.dummy = randtRowGenerator_2.getRandomdummy();
					nb_line_tRowGenerator_2++;

					log.debug("tRowGenerator_2 - Retrieving the record "
							+ nb_line_tRowGenerator_2 + ".");

					/**
					 * [tRowGenerator_2 begin ] stop
					 */

					/**
					 * [tRowGenerator_2 main ] start
					 */

					currentComponent = "tRowGenerator_2";

					tos_count_tRowGenerator_2++;

					/**
					 * [tRowGenerator_2 main ] stop
					 */

					/**
					 * [tRowGenerator_2 process_data_begin ] start
					 */

					currentComponent = "tRowGenerator_2";

					/**
					 * [tRowGenerator_2 process_data_begin ] stop
					 */

					/**
					 * [tFileOutputDelimited_2 main ] start
					 */

					currentComponent = "tFileOutputDelimited_2";

					// row2
					// row2

					if (execStat) {
						runStat.updateStatOnConnection("row2" + iterateId, 1, 1);
					}

					if (log.isTraceEnabled()) {
						log.trace("row2 - "
								+ (row2 == null ? "" : row2.toLogString()));
					}

					StringBuilder sb_tFileOutputDelimited_2 = new StringBuilder();
					if (row2.dummy != null) {
						sb_tFileOutputDelimited_2.append(row2.dummy);
					}
					sb_tFileOutputDelimited_2
							.append(OUT_DELIM_ROWSEP_tFileOutputDelimited_2);

					synchronized (multiThreadLockWrite) {
						nb_line_tFileOutputDelimited_2++;
						resourceMap.put("nb_line_tFileOutputDelimited_2",
								nb_line_tFileOutputDelimited_2);

						outtFileOutputDelimited_2
								.write(sb_tFileOutputDelimited_2.toString());
						log.debug("tFileOutputDelimited_2 - Writing the record "
								+ nb_line_tFileOutputDelimited_2 + ".");

					}

					tos_count_tFileOutputDelimited_2++;

					/**
					 * [tFileOutputDelimited_2 main ] stop
					 */

					/**
					 * [tFileOutputDelimited_2 process_data_begin ] start
					 */

					currentComponent = "tFileOutputDelimited_2";

					/**
					 * [tFileOutputDelimited_2 process_data_begin ] stop
					 */

					/**
					 * [tFileOutputDelimited_2 process_data_end ] start
					 */

					currentComponent = "tFileOutputDelimited_2";

					/**
					 * [tFileOutputDelimited_2 process_data_end ] stop
					 */

					/**
					 * [tRowGenerator_2 process_data_end ] start
					 */

					currentComponent = "tRowGenerator_2";

					/**
					 * [tRowGenerator_2 process_data_end ] stop
					 */

					/**
					 * [tRowGenerator_2 end ] start
					 */

					currentComponent = "tRowGenerator_2";

				}
				globalMap.put("tRowGenerator_2_NB_LINE",
						nb_line_tRowGenerator_2);
				log.info("tRowGenerator_2 - Generated records count:"
						+ nb_line_tRowGenerator_2 + " .");

				if (log.isDebugEnabled())
					log.debug("tRowGenerator_2 - " + ("Done."));

				ok_Hash.put("tRowGenerator_2", true);
				end_Hash.put("tRowGenerator_2", System.currentTimeMillis());

				talendStats_STATS.addMessage(
						"end",
						"tRowGenerator_2",
						end_Hash.get("tRowGenerator_2")
								- start_Hash.get("tRowGenerator_2"));
				talendStats_STATSProcess(globalMap);

				/**
				 * [tRowGenerator_2 end ] stop
				 */

				/**
				 * [tFileOutputDelimited_2 end ] start
				 */

				currentComponent = "tFileOutputDelimited_2";

				synchronized (multiThreadLockWrite) {

					if (outtFileOutputDelimited_2 != null) {
						outtFileOutputDelimited_2.flush();
						outtFileOutputDelimited_2.close();
					}

					globalMap.put("tFileOutputDelimited_2_NB_LINE",
							nb_line_tFileOutputDelimited_2);
					globalMap.put("tFileOutputDelimited_2_FILE_NAME",
							fileName_tFileOutputDelimited_2);

				}

				resourceMap.put("finish_tFileOutputDelimited_2", true);

				log.debug("tFileOutputDelimited_2 - Written records count: "
						+ nb_line_tFileOutputDelimited_2 + " .");

				if (execStat) {
					if (resourceMap.get("inIterateVComp") == null
							|| !((Boolean) resourceMap.get("inIterateVComp"))) {
						runStat.updateStatOnConnection("row2" + iterateId, 2, 0);
					}
				}

				if (log.isDebugEnabled())
					log.debug("tFileOutputDelimited_2 - " + ("Done."));

				ok_Hash.put("tFileOutputDelimited_2", true);
				end_Hash.put("tFileOutputDelimited_2",
						System.currentTimeMillis());

				talendStats_STATS.addMessage(
						"end",
						"tFileOutputDelimited_2",
						end_Hash.get("tFileOutputDelimited_2")
								- start_Hash.get("tFileOutputDelimited_2"));
				talendStats_STATSProcess(globalMap);

				/**
				 * [tFileOutputDelimited_2 end ] stop
				 */

			}// end the resume

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent,
					globalMap);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [tRowGenerator_2 finally ] start
				 */

				currentComponent = "tRowGenerator_2";

				/**
				 * [tRowGenerator_2 finally ] stop
				 */

				/**
				 * [tFileOutputDelimited_2 finally ] start
				 */

				currentComponent = "tFileOutputDelimited_2";

				if (resourceMap.get("finish_tFileOutputDelimited_2") == null) {

					synchronized (multiThreadLockWrite) {

						java.io.Writer outtFileOutputDelimited_2 = (java.io.Writer) resourceMap
								.get("out_tFileOutputDelimited_2");
						if (outtFileOutputDelimited_2 != null) {
							outtFileOutputDelimited_2.flush();
							outtFileOutputDelimited_2.close();
						}

					}

				}

				/**
				 * [tFileOutputDelimited_2 finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tRowGenerator_2_SUBPROCESS_STATE", 1);
	}

	public static class row_talendStats_STATSStruct implements
			routines.system.IPersistableRow<row_talendStats_STATSStruct> {
		final static byte[] commonByteArrayLock_DEMOTALEND_BigFiles = new byte[0];
		static byte[] commonByteArray_DEMOTALEND_BigFiles = new byte[0];

		public java.util.Date moment;

		public java.util.Date getMoment() {
			return this.moment;
		}

		public String pid;

		public String getPid() {
			return this.pid;
		}

		public String father_pid;

		public String getFather_pid() {
			return this.father_pid;
		}

		public String root_pid;

		public String getRoot_pid() {
			return this.root_pid;
		}

		public Long system_pid;

		public Long getSystem_pid() {
			return this.system_pid;
		}

		public String project;

		public String getProject() {
			return this.project;
		}

		public String job;

		public String getJob() {
			return this.job;
		}

		public String job_repository_id;

		public String getJob_repository_id() {
			return this.job_repository_id;
		}

		public String job_version;

		public String getJob_version() {
			return this.job_version;
		}

		public String context;

		public String getContext() {
			return this.context;
		}

		public String origin;

		public String getOrigin() {
			return this.origin;
		}

		public String message_type;

		public String getMessage_type() {
			return this.message_type;
		}

		public String message;

		public String getMessage() {
			return this.message;
		}

		public Long duration;

		public Long getDuration() {
			return this.duration;
		}

		private java.util.Date readDate(ObjectInputStream dis)
				throws IOException {
			java.util.Date dateReturn = null;
			int length = 0;
			length = dis.readByte();
			if (length == -1) {
				dateReturn = null;
			} else {
				dateReturn = new Date(dis.readLong());
			}
			return dateReturn;
		}

		private void writeDate(java.util.Date date1, ObjectOutputStream dos)
				throws IOException {
			if (date1 == null) {
				dos.writeByte(-1);
			} else {
				dos.writeByte(0);
				dos.writeLong(date1.getTime());
			}
		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_DEMOTALEND_BigFiles.length) {
					if (length < 1024
							&& commonByteArray_DEMOTALEND_BigFiles.length == 0) {
						commonByteArray_DEMOTALEND_BigFiles = new byte[1024];
					} else {
						commonByteArray_DEMOTALEND_BigFiles = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_DEMOTALEND_BigFiles, 0, length);
				strReturn = new String(commonByteArray_DEMOTALEND_BigFiles, 0,
						length, utf8Charset);
			}
			return strReturn;
		}

		private void writeString(String str, ObjectOutputStream dos)
				throws IOException {
			if (str == null) {
				dos.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				dos.writeInt(byteArray.length);
				dos.write(byteArray);
			}
		}

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_DEMOTALEND_BigFiles) {

				try {

					int length = 0;

					this.moment = readDate(dis);

					this.pid = readString(dis);

					this.father_pid = readString(dis);

					this.root_pid = readString(dis);

					length = dis.readByte();
					if (length == -1) {
						this.system_pid = null;
					} else {
						this.system_pid = dis.readLong();
					}

					this.project = readString(dis);

					this.job = readString(dis);

					this.job_repository_id = readString(dis);

					this.job_version = readString(dis);

					this.context = readString(dis);

					this.origin = readString(dis);

					this.message_type = readString(dis);

					this.message = readString(dis);

					length = dis.readByte();
					if (length == -1) {
						this.duration = null;
					} else {
						this.duration = dis.readLong();
					}

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// java.util.Date

				writeDate(this.moment, dos);

				// String

				writeString(this.pid, dos);

				// String

				writeString(this.father_pid, dos);

				// String

				writeString(this.root_pid, dos);

				// Long

				if (this.system_pid == null) {
					dos.writeByte(-1);
				} else {
					dos.writeByte(0);
					dos.writeLong(this.system_pid);
				}

				// String

				writeString(this.project, dos);

				// String

				writeString(this.job, dos);

				// String

				writeString(this.job_repository_id, dos);

				// String

				writeString(this.job_version, dos);

				// String

				writeString(this.context, dos);

				// String

				writeString(this.origin, dos);

				// String

				writeString(this.message_type, dos);

				// String

				writeString(this.message, dos);

				// Long

				if (this.duration == null) {
					dos.writeByte(-1);
				} else {
					dos.writeByte(0);
					dos.writeLong(this.duration);
				}

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("moment=" + String.valueOf(moment));
			sb.append(",pid=" + pid);
			sb.append(",father_pid=" + father_pid);
			sb.append(",root_pid=" + root_pid);
			sb.append(",system_pid=" + String.valueOf(system_pid));
			sb.append(",project=" + project);
			sb.append(",job=" + job);
			sb.append(",job_repository_id=" + job_repository_id);
			sb.append(",job_version=" + job_version);
			sb.append(",context=" + context);
			sb.append(",origin=" + origin);
			sb.append(",message_type=" + message_type);
			sb.append(",message=" + message);
			sb.append(",duration=" + String.valueOf(duration));
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (moment == null) {
				sb.append("<null>");
			} else {
				sb.append(moment);
			}

			sb.append("|");

			if (pid == null) {
				sb.append("<null>");
			} else {
				sb.append(pid);
			}

			sb.append("|");

			if (father_pid == null) {
				sb.append("<null>");
			} else {
				sb.append(father_pid);
			}

			sb.append("|");

			if (root_pid == null) {
				sb.append("<null>");
			} else {
				sb.append(root_pid);
			}

			sb.append("|");

			if (system_pid == null) {
				sb.append("<null>");
			} else {
				sb.append(system_pid);
			}

			sb.append("|");

			if (project == null) {
				sb.append("<null>");
			} else {
				sb.append(project);
			}

			sb.append("|");

			if (job == null) {
				sb.append("<null>");
			} else {
				sb.append(job);
			}

			sb.append("|");

			if (job_repository_id == null) {
				sb.append("<null>");
			} else {
				sb.append(job_repository_id);
			}

			sb.append("|");

			if (job_version == null) {
				sb.append("<null>");
			} else {
				sb.append(job_version);
			}

			sb.append("|");

			if (context == null) {
				sb.append("<null>");
			} else {
				sb.append(context);
			}

			sb.append("|");

			if (origin == null) {
				sb.append("<null>");
			} else {
				sb.append(origin);
			}

			sb.append("|");

			if (message_type == null) {
				sb.append("<null>");
			} else {
				sb.append(message_type);
			}

			sb.append("|");

			if (message == null) {
				sb.append("<null>");
			} else {
				sb.append(message);
			}

			sb.append("|");

			if (duration == null) {
				sb.append("<null>");
			} else {
				sb.append(duration);
			}

			sb.append("|");

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(row_talendStats_STATSStruct other) {

			int returnValue = -1;

			return returnValue;
		}

		private int checkNullsAndCompare(Object object1, Object object2) {
			int returnValue = 0;
			if (object1 instanceof Comparable && object2 instanceof Comparable) {
				returnValue = ((Comparable) object1).compareTo(object2);
			} else if (object1 != null && object2 != null) {
				returnValue = compareStrings(object1.toString(),
						object2.toString());
			} else if (object1 == null && object2 != null) {
				returnValue = 1;
			} else if (object1 != null && object2 == null) {
				returnValue = -1;
			} else {
				returnValue = 0;
			}

			return returnValue;
		}

		private int compareStrings(String string1, String string2) {
			return string1.compareTo(string2);
		}

	}

	public void talendStats_STATSProcess(
			final java.util.Map<String, Object> globalMap)
			throws TalendException {
		globalMap.put("talendStats_STATS_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;
		String currentVirtualComponent = null;

		String iterateId = "";

		String currentComponent = "";
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception()
						.getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { // start the resume
				globalResumeTicket = true;

				row_talendStats_STATSStruct row_talendStats_STATS = new row_talendStats_STATSStruct();

				/**
				 * [talendStats_FILE begin ] start
				 */

				ok_Hash.put("talendStats_FILE", false);
				start_Hash.put("talendStats_FILE", System.currentTimeMillis());

				currentVirtualComponent = "talendStats_FILE";

				currentComponent = "talendStats_FILE";

				if (execStat) {
					if (resourceMap.get("inIterateVComp") == null) {

						runStat.updateStatOnConnection("Main" + iterateId, 0, 0);

					}
				}

				int tos_count_talendStats_FILE = 0;

				if (log.isDebugEnabled())
					log.debug("talendStats_FILE - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_talendStats_FILE {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_talendStats_FILE = new StringBuilder();
							log4jParamters_talendStats_FILE
									.append("Parameters:");
							log4jParamters_talendStats_FILE.append("USESTREAM"
									+ " = " + "false");
							log4jParamters_talendStats_FILE.append(" | ");
							log4jParamters_talendStats_FILE
									.append("FILENAME"
											+ " = "
											+ "\"C:/Talend-Studio-20181026_1147-V7.1.1/Talend-Studio-20181026_1147-V7.1.1/workspace/.metadata\"+ \"/\" +\"stats_file.txt\"");
							log4jParamters_talendStats_FILE.append(" | ");
							log4jParamters_talendStats_FILE
									.append("ROWSEPARATOR" + " = " + "\"\\n\"");
							log4jParamters_talendStats_FILE.append(" | ");
							log4jParamters_talendStats_FILE
									.append("FIELDSEPARATOR" + " = " + "\";\"");
							log4jParamters_talendStats_FILE.append(" | ");
							log4jParamters_talendStats_FILE.append("APPEND"
									+ " = " + "true");
							log4jParamters_talendStats_FILE.append(" | ");
							log4jParamters_talendStats_FILE
									.append("INCLUDEHEADER" + " = " + "false");
							log4jParamters_talendStats_FILE.append(" | ");
							log4jParamters_talendStats_FILE
									.append("ADVANCED_SEPARATOR" + " = "
											+ "false");
							log4jParamters_talendStats_FILE.append(" | ");
							log4jParamters_talendStats_FILE.append("CSV_OPTION"
									+ " = " + "false");
							log4jParamters_talendStats_FILE.append(" | ");
							log4jParamters_talendStats_FILE.append("CREATE"
									+ " = " + "true");
							log4jParamters_talendStats_FILE.append(" | ");
							log4jParamters_talendStats_FILE.append("SPLIT"
									+ " = " + "false");
							log4jParamters_talendStats_FILE.append(" | ");
							log4jParamters_talendStats_FILE.append("FLUSHONROW"
									+ " = " + "false");
							log4jParamters_talendStats_FILE.append(" | ");
							log4jParamters_talendStats_FILE.append("ROW_MODE"
									+ " = " + "false");
							log4jParamters_talendStats_FILE.append(" | ");
							log4jParamters_talendStats_FILE.append("ENCODING"
									+ " = " + "\"ISO-8859-15\"");
							log4jParamters_talendStats_FILE.append(" | ");
							log4jParamters_talendStats_FILE
									.append("DELETE_EMPTYFILE" + " = "
											+ "false");
							log4jParamters_talendStats_FILE.append(" | ");
							if (log.isDebugEnabled())
								log.debug("talendStats_FILE - "
										+ (log4jParamters_talendStats_FILE));
						}
					}
					new BytesLimit65535_talendStats_FILE().limitLog4jByte();
				}

				String fileName_talendStats_FILE = "";
				fileName_talendStats_FILE = (new java.io.File(
						"C:/Talend-Studio-20181026_1147-V7.1.1/Talend-Studio-20181026_1147-V7.1.1/workspace/.metadata"
								+ "/" + "stats_file.txt")).getAbsolutePath()
						.replace("\\", "/");
				String fullName_talendStats_FILE = null;
				String extension_talendStats_FILE = null;
				String directory_talendStats_FILE = null;
				if ((fileName_talendStats_FILE.indexOf("/") != -1)) {
					if (fileName_talendStats_FILE.lastIndexOf(".") < fileName_talendStats_FILE
							.lastIndexOf("/")) {
						fullName_talendStats_FILE = fileName_talendStats_FILE;
						extension_talendStats_FILE = "";
					} else {
						fullName_talendStats_FILE = fileName_talendStats_FILE
								.substring(0, fileName_talendStats_FILE
										.lastIndexOf("."));
						extension_talendStats_FILE = fileName_talendStats_FILE
								.substring(fileName_talendStats_FILE
										.lastIndexOf("."));
					}
					directory_talendStats_FILE = fileName_talendStats_FILE
							.substring(0,
									fileName_talendStats_FILE.lastIndexOf("/"));
				} else {
					if (fileName_talendStats_FILE.lastIndexOf(".") != -1) {
						fullName_talendStats_FILE = fileName_talendStats_FILE
								.substring(0, fileName_talendStats_FILE
										.lastIndexOf("."));
						extension_talendStats_FILE = fileName_talendStats_FILE
								.substring(fileName_talendStats_FILE
										.lastIndexOf("."));
					} else {
						fullName_talendStats_FILE = fileName_talendStats_FILE;
						extension_talendStats_FILE = "";
					}
					directory_talendStats_FILE = "";
				}
				boolean isFileGenerated_talendStats_FILE = true;
				java.io.File filetalendStats_FILE = new java.io.File(
						fileName_talendStats_FILE);
				globalMap.put("talendStats_FILE_FILE_NAME",
						fileName_talendStats_FILE);
				if (filetalendStats_FILE.exists()) {
					isFileGenerated_talendStats_FILE = false;
				}
				int nb_line_talendStats_FILE = 0;
				int splitedFileNo_talendStats_FILE = 0;
				int currentRow_talendStats_FILE = 0;

				final String OUT_DELIM_talendStats_FILE = /**
				 * Start field
				 * talendStats_FILE:FIELDSEPARATOR
				 */
				";"/** End field talendStats_FILE:FIELDSEPARATOR */
				;

				final String OUT_DELIM_ROWSEP_talendStats_FILE = /**
				 * Start field
				 * talendStats_FILE:ROWSEPARATOR
				 */
				"\n"/** End field talendStats_FILE:ROWSEPARATOR */
				;

				// create directory only if not exists
				if (directory_talendStats_FILE != null
						&& directory_talendStats_FILE.trim().length() != 0) {
					java.io.File dir_talendStats_FILE = new java.io.File(
							directory_talendStats_FILE);
					if (!dir_talendStats_FILE.exists()) {
						log.info("talendStats_FILE - Creating directory '"
								+ dir_talendStats_FILE.getCanonicalPath()
								+ "'.");
						dir_talendStats_FILE.mkdirs();
						log.info("talendStats_FILE - The directory '"
								+ dir_talendStats_FILE.getCanonicalPath()
								+ "' has been created successfully.");
					}
				}

				// routines.system.Row
				java.io.Writer outtalendStats_FILE = null;

				outtalendStats_FILE = new java.io.BufferedWriter(
						new java.io.OutputStreamWriter(
								new java.io.FileOutputStream(
										fileName_talendStats_FILE, true),
								"ISO-8859-15"));

				resourceMap.put("out_talendStats_FILE", outtalendStats_FILE);
				resourceMap.put("nb_line_talendStats_FILE",
						nb_line_talendStats_FILE);

				/**
				 * [talendStats_FILE begin ] stop
				 */

				/**
				 * [talendStats_STATS begin ] start
				 */

				ok_Hash.put("talendStats_STATS", false);
				start_Hash.put("talendStats_STATS", System.currentTimeMillis());

				currentVirtualComponent = "talendStats_STATS";

				currentComponent = "talendStats_STATS";

				int tos_count_talendStats_STATS = 0;

				if (log.isDebugEnabled())
					log.debug("talendStats_STATS - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_talendStats_STATS {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_talendStats_STATS = new StringBuilder();
							log4jParamters_talendStats_STATS
									.append("Parameters:");
							if (log.isDebugEnabled())
								log.debug("talendStats_STATS - "
										+ (log4jParamters_talendStats_STATS));
						}
					}
					new BytesLimit65535_talendStats_STATS().limitLog4jByte();
				}

				for (StatCatcherUtils.StatCatcherMessage scm : talendStats_STATS
						.getMessages()) {
					row_talendStats_STATS.pid = pid;
					row_talendStats_STATS.root_pid = rootPid;
					row_talendStats_STATS.father_pid = fatherPid;
					row_talendStats_STATS.project = projectName;
					row_talendStats_STATS.job = jobName;
					row_talendStats_STATS.context = contextStr;
					row_talendStats_STATS.origin = (scm.getOrigin() == null
							|| scm.getOrigin().length() < 1 ? null : scm
							.getOrigin());
					row_talendStats_STATS.message = scm.getMessage();
					row_talendStats_STATS.duration = scm.getDuration();
					row_talendStats_STATS.moment = scm.getMoment();
					row_talendStats_STATS.message_type = scm.getMessageType();
					row_talendStats_STATS.job_version = scm.getJobVersion();
					row_talendStats_STATS.job_repository_id = scm.getJobId();
					row_talendStats_STATS.system_pid = scm.getSystemPid();

					/**
					 * [talendStats_STATS begin ] stop
					 */

					/**
					 * [talendStats_STATS main ] start
					 */

					currentVirtualComponent = "talendStats_STATS";

					currentComponent = "talendStats_STATS";

					tos_count_talendStats_STATS++;

					/**
					 * [talendStats_STATS main ] stop
					 */

					/**
					 * [talendStats_STATS process_data_begin ] start
					 */

					currentVirtualComponent = "talendStats_STATS";

					currentComponent = "talendStats_STATS";

					/**
					 * [talendStats_STATS process_data_begin ] stop
					 */

					/**
					 * [talendStats_FILE main ] start
					 */

					currentVirtualComponent = "talendStats_FILE";

					currentComponent = "talendStats_FILE";

					// Main
					// row_talendStats_STATS

					if (execStat) {
						runStat.updateStatOnConnection("Main" + iterateId, 1, 1);
					}

					StringBuilder sb_talendStats_FILE = new StringBuilder();
					if (row_talendStats_STATS.moment != null) {
						sb_talendStats_FILE.append(FormatterUtils.format_Date(
								row_talendStats_STATS.moment,
								"yyyy-MM-dd HH:mm:ss"));
					}
					sb_talendStats_FILE.append(OUT_DELIM_talendStats_FILE);
					if (row_talendStats_STATS.pid != null) {
						sb_talendStats_FILE.append(row_talendStats_STATS.pid);
					}
					sb_talendStats_FILE.append(OUT_DELIM_talendStats_FILE);
					if (row_talendStats_STATS.father_pid != null) {
						sb_talendStats_FILE
								.append(row_talendStats_STATS.father_pid);
					}
					sb_talendStats_FILE.append(OUT_DELIM_talendStats_FILE);
					if (row_talendStats_STATS.root_pid != null) {
						sb_talendStats_FILE
								.append(row_talendStats_STATS.root_pid);
					}
					sb_talendStats_FILE.append(OUT_DELIM_talendStats_FILE);
					if (row_talendStats_STATS.system_pid != null) {
						sb_talendStats_FILE
								.append(row_talendStats_STATS.system_pid);
					}
					sb_talendStats_FILE.append(OUT_DELIM_talendStats_FILE);
					if (row_talendStats_STATS.project != null) {
						sb_talendStats_FILE
								.append(row_talendStats_STATS.project);
					}
					sb_talendStats_FILE.append(OUT_DELIM_talendStats_FILE);
					if (row_talendStats_STATS.job != null) {
						sb_talendStats_FILE.append(row_talendStats_STATS.job);
					}
					sb_talendStats_FILE.append(OUT_DELIM_talendStats_FILE);
					if (row_talendStats_STATS.job_repository_id != null) {
						sb_talendStats_FILE
								.append(row_talendStats_STATS.job_repository_id);
					}
					sb_talendStats_FILE.append(OUT_DELIM_talendStats_FILE);
					if (row_talendStats_STATS.job_version != null) {
						sb_talendStats_FILE
								.append(row_talendStats_STATS.job_version);
					}
					sb_talendStats_FILE.append(OUT_DELIM_talendStats_FILE);
					if (row_talendStats_STATS.context != null) {
						sb_talendStats_FILE
								.append(row_talendStats_STATS.context);
					}
					sb_talendStats_FILE.append(OUT_DELIM_talendStats_FILE);
					if (row_talendStats_STATS.origin != null) {
						sb_talendStats_FILE
								.append(row_talendStats_STATS.origin);
					}
					sb_talendStats_FILE.append(OUT_DELIM_talendStats_FILE);
					if (row_talendStats_STATS.message_type != null) {
						sb_talendStats_FILE
								.append(row_talendStats_STATS.message_type);
					}
					sb_talendStats_FILE.append(OUT_DELIM_talendStats_FILE);
					if (row_talendStats_STATS.message != null) {
						sb_talendStats_FILE
								.append(row_talendStats_STATS.message);
					}
					sb_talendStats_FILE.append(OUT_DELIM_talendStats_FILE);
					if (row_talendStats_STATS.duration != null) {
						sb_talendStats_FILE
								.append(row_talendStats_STATS.duration);
					}
					sb_talendStats_FILE
							.append(OUT_DELIM_ROWSEP_talendStats_FILE);

					synchronized (multiThreadLockWrite) {
						nb_line_talendStats_FILE++;
						resourceMap.put("nb_line_talendStats_FILE",
								nb_line_talendStats_FILE);

						outtalendStats_FILE.write(sb_talendStats_FILE
								.toString());
						log.debug("talendStats_FILE - Writing the record "
								+ nb_line_talendStats_FILE + ".");

					}

					tos_count_talendStats_FILE++;

					/**
					 * [talendStats_FILE main ] stop
					 */

					/**
					 * [talendStats_FILE process_data_begin ] start
					 */

					currentVirtualComponent = "talendStats_FILE";

					currentComponent = "talendStats_FILE";

					/**
					 * [talendStats_FILE process_data_begin ] stop
					 */

					/**
					 * [talendStats_FILE process_data_end ] start
					 */

					currentVirtualComponent = "talendStats_FILE";

					currentComponent = "talendStats_FILE";

					/**
					 * [talendStats_FILE process_data_end ] stop
					 */

					/**
					 * [talendStats_STATS process_data_end ] start
					 */

					currentVirtualComponent = "talendStats_STATS";

					currentComponent = "talendStats_STATS";

					/**
					 * [talendStats_STATS process_data_end ] stop
					 */

					/**
					 * [talendStats_STATS end ] start
					 */

					currentVirtualComponent = "talendStats_STATS";

					currentComponent = "talendStats_STATS";

				}

				if (log.isDebugEnabled())
					log.debug("talendStats_STATS - " + ("Done."));

				ok_Hash.put("talendStats_STATS", true);
				end_Hash.put("talendStats_STATS", System.currentTimeMillis());

				/**
				 * [talendStats_STATS end ] stop
				 */

				/**
				 * [talendStats_FILE end ] start
				 */

				currentVirtualComponent = "talendStats_FILE";

				currentComponent = "talendStats_FILE";

				synchronized (multiThreadLockWrite) {

					if (outtalendStats_FILE != null) {
						outtalendStats_FILE.flush();
						outtalendStats_FILE.close();
					}

					globalMap.put("talendStats_FILE_NB_LINE",
							nb_line_talendStats_FILE);
					globalMap.put("talendStats_FILE_FILE_NAME",
							fileName_talendStats_FILE);

				}

				resourceMap.put("finish_talendStats_FILE", true);

				log.debug("talendStats_FILE - Written records count: "
						+ nb_line_talendStats_FILE + " .");

				if (execStat) {
					if (resourceMap.get("inIterateVComp") == null
							|| !((Boolean) resourceMap.get("inIterateVComp"))) {
						runStat.updateStatOnConnection("Main" + iterateId, 2, 0);
					}
				}

				if (log.isDebugEnabled())
					log.debug("talendStats_FILE - " + ("Done."));

				ok_Hash.put("talendStats_FILE", true);
				end_Hash.put("talendStats_FILE", System.currentTimeMillis());

				/**
				 * [talendStats_FILE end ] stop
				 */

			}// end the resume

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent,
					globalMap);

			te.setVirtualComponentName(currentVirtualComponent);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [talendStats_STATS finally ] start
				 */

				currentVirtualComponent = "talendStats_STATS";

				currentComponent = "talendStats_STATS";

				/**
				 * [talendStats_STATS finally ] stop
				 */

				/**
				 * [talendStats_FILE finally ] start
				 */

				currentVirtualComponent = "talendStats_FILE";

				currentComponent = "talendStats_FILE";

				if (resourceMap.get("finish_talendStats_FILE") == null) {

					synchronized (multiThreadLockWrite) {

						java.io.Writer outtalendStats_FILE = (java.io.Writer) resourceMap
								.get("out_talendStats_FILE");
						if (outtalendStats_FILE != null) {
							outtalendStats_FILE.flush();
							outtalendStats_FILE.close();
						}

					}

				}

				/**
				 * [talendStats_FILE finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("talendStats_STATS_SUBPROCESS_STATE", 1);
	}

	public static class row_talendLogs_LOGSStruct implements
			routines.system.IPersistableRow<row_talendLogs_LOGSStruct> {
		final static byte[] commonByteArrayLock_DEMOTALEND_BigFiles = new byte[0];
		static byte[] commonByteArray_DEMOTALEND_BigFiles = new byte[0];

		public java.util.Date moment;

		public java.util.Date getMoment() {
			return this.moment;
		}

		public String pid;

		public String getPid() {
			return this.pid;
		}

		public String root_pid;

		public String getRoot_pid() {
			return this.root_pid;
		}

		public String father_pid;

		public String getFather_pid() {
			return this.father_pid;
		}

		public String project;

		public String getProject() {
			return this.project;
		}

		public String job;

		public String getJob() {
			return this.job;
		}

		public String context;

		public String getContext() {
			return this.context;
		}

		public Integer priority;

		public Integer getPriority() {
			return this.priority;
		}

		public String type;

		public String getType() {
			return this.type;
		}

		public String origin;

		public String getOrigin() {
			return this.origin;
		}

		public String message;

		public String getMessage() {
			return this.message;
		}

		public Integer code;

		public Integer getCode() {
			return this.code;
		}

		private java.util.Date readDate(ObjectInputStream dis)
				throws IOException {
			java.util.Date dateReturn = null;
			int length = 0;
			length = dis.readByte();
			if (length == -1) {
				dateReturn = null;
			} else {
				dateReturn = new Date(dis.readLong());
			}
			return dateReturn;
		}

		private void writeDate(java.util.Date date1, ObjectOutputStream dos)
				throws IOException {
			if (date1 == null) {
				dos.writeByte(-1);
			} else {
				dos.writeByte(0);
				dos.writeLong(date1.getTime());
			}
		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_DEMOTALEND_BigFiles.length) {
					if (length < 1024
							&& commonByteArray_DEMOTALEND_BigFiles.length == 0) {
						commonByteArray_DEMOTALEND_BigFiles = new byte[1024];
					} else {
						commonByteArray_DEMOTALEND_BigFiles = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_DEMOTALEND_BigFiles, 0, length);
				strReturn = new String(commonByteArray_DEMOTALEND_BigFiles, 0,
						length, utf8Charset);
			}
			return strReturn;
		}

		private void writeString(String str, ObjectOutputStream dos)
				throws IOException {
			if (str == null) {
				dos.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				dos.writeInt(byteArray.length);
				dos.write(byteArray);
			}
		}

		private Integer readInteger(ObjectInputStream dis) throws IOException {
			Integer intReturn;
			int length = 0;
			length = dis.readByte();
			if (length == -1) {
				intReturn = null;
			} else {
				intReturn = dis.readInt();
			}
			return intReturn;
		}

		private void writeInteger(Integer intNum, ObjectOutputStream dos)
				throws IOException {
			if (intNum == null) {
				dos.writeByte(-1);
			} else {
				dos.writeByte(0);
				dos.writeInt(intNum);
			}
		}

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_DEMOTALEND_BigFiles) {

				try {

					int length = 0;

					this.moment = readDate(dis);

					this.pid = readString(dis);

					this.root_pid = readString(dis);

					this.father_pid = readString(dis);

					this.project = readString(dis);

					this.job = readString(dis);

					this.context = readString(dis);

					this.priority = readInteger(dis);

					this.type = readString(dis);

					this.origin = readString(dis);

					this.message = readString(dis);

					this.code = readInteger(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// java.util.Date

				writeDate(this.moment, dos);

				// String

				writeString(this.pid, dos);

				// String

				writeString(this.root_pid, dos);

				// String

				writeString(this.father_pid, dos);

				// String

				writeString(this.project, dos);

				// String

				writeString(this.job, dos);

				// String

				writeString(this.context, dos);

				// Integer

				writeInteger(this.priority, dos);

				// String

				writeString(this.type, dos);

				// String

				writeString(this.origin, dos);

				// String

				writeString(this.message, dos);

				// Integer

				writeInteger(this.code, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("moment=" + String.valueOf(moment));
			sb.append(",pid=" + pid);
			sb.append(",root_pid=" + root_pid);
			sb.append(",father_pid=" + father_pid);
			sb.append(",project=" + project);
			sb.append(",job=" + job);
			sb.append(",context=" + context);
			sb.append(",priority=" + String.valueOf(priority));
			sb.append(",type=" + type);
			sb.append(",origin=" + origin);
			sb.append(",message=" + message);
			sb.append(",code=" + String.valueOf(code));
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (moment == null) {
				sb.append("<null>");
			} else {
				sb.append(moment);
			}

			sb.append("|");

			if (pid == null) {
				sb.append("<null>");
			} else {
				sb.append(pid);
			}

			sb.append("|");

			if (root_pid == null) {
				sb.append("<null>");
			} else {
				sb.append(root_pid);
			}

			sb.append("|");

			if (father_pid == null) {
				sb.append("<null>");
			} else {
				sb.append(father_pid);
			}

			sb.append("|");

			if (project == null) {
				sb.append("<null>");
			} else {
				sb.append(project);
			}

			sb.append("|");

			if (job == null) {
				sb.append("<null>");
			} else {
				sb.append(job);
			}

			sb.append("|");

			if (context == null) {
				sb.append("<null>");
			} else {
				sb.append(context);
			}

			sb.append("|");

			if (priority == null) {
				sb.append("<null>");
			} else {
				sb.append(priority);
			}

			sb.append("|");

			if (type == null) {
				sb.append("<null>");
			} else {
				sb.append(type);
			}

			sb.append("|");

			if (origin == null) {
				sb.append("<null>");
			} else {
				sb.append(origin);
			}

			sb.append("|");

			if (message == null) {
				sb.append("<null>");
			} else {
				sb.append(message);
			}

			sb.append("|");

			if (code == null) {
				sb.append("<null>");
			} else {
				sb.append(code);
			}

			sb.append("|");

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(row_talendLogs_LOGSStruct other) {

			int returnValue = -1;

			return returnValue;
		}

		private int checkNullsAndCompare(Object object1, Object object2) {
			int returnValue = 0;
			if (object1 instanceof Comparable && object2 instanceof Comparable) {
				returnValue = ((Comparable) object1).compareTo(object2);
			} else if (object1 != null && object2 != null) {
				returnValue = compareStrings(object1.toString(),
						object2.toString());
			} else if (object1 == null && object2 != null) {
				returnValue = 1;
			} else if (object1 != null && object2 == null) {
				returnValue = -1;
			} else {
				returnValue = 0;
			}

			return returnValue;
		}

		private int compareStrings(String string1, String string2) {
			return string1.compareTo(string2);
		}

	}

	public void talendLogs_LOGSProcess(
			final java.util.Map<String, Object> globalMap)
			throws TalendException {
		globalMap.put("talendLogs_LOGS_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;
		String currentVirtualComponent = null;

		String iterateId = "";

		String currentComponent = "";
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception()
						.getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { // start the resume
				globalResumeTicket = true;

				row_talendLogs_LOGSStruct row_talendLogs_LOGS = new row_talendLogs_LOGSStruct();

				/**
				 * [talendLogs_FILE begin ] start
				 */

				ok_Hash.put("talendLogs_FILE", false);
				start_Hash.put("talendLogs_FILE", System.currentTimeMillis());

				currentVirtualComponent = "talendLogs_FILE";

				currentComponent = "talendLogs_FILE";

				if (execStat) {
					if (resourceMap.get("inIterateVComp") == null) {

						runStat.updateStatOnConnection("Main" + iterateId, 0, 0);

					}
				}

				int tos_count_talendLogs_FILE = 0;

				if (log.isDebugEnabled())
					log.debug("talendLogs_FILE - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_talendLogs_FILE {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_talendLogs_FILE = new StringBuilder();
							log4jParamters_talendLogs_FILE
									.append("Parameters:");
							log4jParamters_talendLogs_FILE.append("USESTREAM"
									+ " = " + "false");
							log4jParamters_talendLogs_FILE.append(" | ");
							log4jParamters_talendLogs_FILE
									.append("FILENAME"
											+ " = "
											+ "\"C:/Talend-Studio-20181026_1147-V7.1.1/Talend-Studio-20181026_1147-V7.1.1/workspace/.metadata\"+ \"/\" +\"logs_file.txt\"");
							log4jParamters_talendLogs_FILE.append(" | ");
							log4jParamters_talendLogs_FILE
									.append("ROWSEPARATOR" + " = " + "\"\\n\"");
							log4jParamters_talendLogs_FILE.append(" | ");
							log4jParamters_talendLogs_FILE
									.append("FIELDSEPARATOR" + " = " + "\";\"");
							log4jParamters_talendLogs_FILE.append(" | ");
							log4jParamters_talendLogs_FILE.append("APPEND"
									+ " = " + "true");
							log4jParamters_talendLogs_FILE.append(" | ");
							log4jParamters_talendLogs_FILE
									.append("INCLUDEHEADER" + " = " + "false");
							log4jParamters_talendLogs_FILE.append(" | ");
							log4jParamters_talendLogs_FILE
									.append("ADVANCED_SEPARATOR" + " = "
											+ "false");
							log4jParamters_talendLogs_FILE.append(" | ");
							log4jParamters_talendLogs_FILE.append("CSV_OPTION"
									+ " = " + "false");
							log4jParamters_talendLogs_FILE.append(" | ");
							log4jParamters_talendLogs_FILE.append("CREATE"
									+ " = " + "true");
							log4jParamters_talendLogs_FILE.append(" | ");
							log4jParamters_talendLogs_FILE.append("SPLIT"
									+ " = " + "false");
							log4jParamters_talendLogs_FILE.append(" | ");
							log4jParamters_talendLogs_FILE.append("FLUSHONROW"
									+ " = " + "false");
							log4jParamters_talendLogs_FILE.append(" | ");
							log4jParamters_talendLogs_FILE.append("ROW_MODE"
									+ " = " + "false");
							log4jParamters_talendLogs_FILE.append(" | ");
							log4jParamters_talendLogs_FILE.append("ENCODING"
									+ " = " + "\"ISO-8859-15\"");
							log4jParamters_talendLogs_FILE.append(" | ");
							log4jParamters_talendLogs_FILE
									.append("DELETE_EMPTYFILE" + " = "
											+ "false");
							log4jParamters_talendLogs_FILE.append(" | ");
							if (log.isDebugEnabled())
								log.debug("talendLogs_FILE - "
										+ (log4jParamters_talendLogs_FILE));
						}
					}
					new BytesLimit65535_talendLogs_FILE().limitLog4jByte();
				}

				String fileName_talendLogs_FILE = "";
				fileName_talendLogs_FILE = (new java.io.File(
						"C:/Talend-Studio-20181026_1147-V7.1.1/Talend-Studio-20181026_1147-V7.1.1/workspace/.metadata"
								+ "/" + "logs_file.txt")).getAbsolutePath()
						.replace("\\", "/");
				String fullName_talendLogs_FILE = null;
				String extension_talendLogs_FILE = null;
				String directory_talendLogs_FILE = null;
				if ((fileName_talendLogs_FILE.indexOf("/") != -1)) {
					if (fileName_talendLogs_FILE.lastIndexOf(".") < fileName_talendLogs_FILE
							.lastIndexOf("/")) {
						fullName_talendLogs_FILE = fileName_talendLogs_FILE;
						extension_talendLogs_FILE = "";
					} else {
						fullName_talendLogs_FILE = fileName_talendLogs_FILE
								.substring(0, fileName_talendLogs_FILE
										.lastIndexOf("."));
						extension_talendLogs_FILE = fileName_talendLogs_FILE
								.substring(fileName_talendLogs_FILE
										.lastIndexOf("."));
					}
					directory_talendLogs_FILE = fileName_talendLogs_FILE
							.substring(0,
									fileName_talendLogs_FILE.lastIndexOf("/"));
				} else {
					if (fileName_talendLogs_FILE.lastIndexOf(".") != -1) {
						fullName_talendLogs_FILE = fileName_talendLogs_FILE
								.substring(0, fileName_talendLogs_FILE
										.lastIndexOf("."));
						extension_talendLogs_FILE = fileName_talendLogs_FILE
								.substring(fileName_talendLogs_FILE
										.lastIndexOf("."));
					} else {
						fullName_talendLogs_FILE = fileName_talendLogs_FILE;
						extension_talendLogs_FILE = "";
					}
					directory_talendLogs_FILE = "";
				}
				boolean isFileGenerated_talendLogs_FILE = true;
				java.io.File filetalendLogs_FILE = new java.io.File(
						fileName_talendLogs_FILE);
				globalMap.put("talendLogs_FILE_FILE_NAME",
						fileName_talendLogs_FILE);
				if (filetalendLogs_FILE.exists()) {
					isFileGenerated_talendLogs_FILE = false;
				}
				int nb_line_talendLogs_FILE = 0;
				int splitedFileNo_talendLogs_FILE = 0;
				int currentRow_talendLogs_FILE = 0;

				final String OUT_DELIM_talendLogs_FILE = /**
				 * Start field
				 * talendLogs_FILE:FIELDSEPARATOR
				 */
				";"/** End field talendLogs_FILE:FIELDSEPARATOR */
				;

				final String OUT_DELIM_ROWSEP_talendLogs_FILE = /**
				 * Start field
				 * talendLogs_FILE:ROWSEPARATOR
				 */
				"\n"/** End field talendLogs_FILE:ROWSEPARATOR */
				;

				// create directory only if not exists
				if (directory_talendLogs_FILE != null
						&& directory_talendLogs_FILE.trim().length() != 0) {
					java.io.File dir_talendLogs_FILE = new java.io.File(
							directory_talendLogs_FILE);
					if (!dir_talendLogs_FILE.exists()) {
						log.info("talendLogs_FILE - Creating directory '"
								+ dir_talendLogs_FILE.getCanonicalPath() + "'.");
						dir_talendLogs_FILE.mkdirs();
						log.info("talendLogs_FILE - The directory '"
								+ dir_talendLogs_FILE.getCanonicalPath()
								+ "' has been created successfully.");
					}
				}

				// routines.system.Row
				java.io.Writer outtalendLogs_FILE = null;

				outtalendLogs_FILE = new java.io.BufferedWriter(
						new java.io.OutputStreamWriter(
								new java.io.FileOutputStream(
										fileName_talendLogs_FILE, true),
								"ISO-8859-15"));

				resourceMap.put("out_talendLogs_FILE", outtalendLogs_FILE);
				resourceMap.put("nb_line_talendLogs_FILE",
						nb_line_talendLogs_FILE);

				/**
				 * [talendLogs_FILE begin ] stop
				 */

				/**
				 * [talendLogs_LOGS begin ] start
				 */

				ok_Hash.put("talendLogs_LOGS", false);
				start_Hash.put("talendLogs_LOGS", System.currentTimeMillis());

				currentVirtualComponent = "talendLogs_LOGS";

				currentComponent = "talendLogs_LOGS";

				int tos_count_talendLogs_LOGS = 0;

				if (log.isDebugEnabled())
					log.debug("talendLogs_LOGS - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_talendLogs_LOGS {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_talendLogs_LOGS = new StringBuilder();
							log4jParamters_talendLogs_LOGS
									.append("Parameters:");
							log4jParamters_talendLogs_LOGS
									.append("CATCH_JAVA_EXCEPTION" + " = "
											+ "true");
							log4jParamters_talendLogs_LOGS.append(" | ");
							log4jParamters_talendLogs_LOGS.append("CATCH_TDIE"
									+ " = " + "true");
							log4jParamters_talendLogs_LOGS.append(" | ");
							log4jParamters_talendLogs_LOGS.append("CATCH_TWARN"
									+ " = " + "true");
							log4jParamters_talendLogs_LOGS.append(" | ");
							log4jParamters_talendLogs_LOGS
									.append("CATCH_TACTIONFAILURE" + " = "
											+ "true");
							log4jParamters_talendLogs_LOGS.append(" | ");
							if (log.isDebugEnabled())
								log.debug("talendLogs_LOGS - "
										+ (log4jParamters_talendLogs_LOGS));
						}
					}
					new BytesLimit65535_talendLogs_LOGS().limitLog4jByte();
				}

				try {
					for (LogCatcherUtils.LogCatcherMessage lcm : talendLogs_LOGS
							.getMessages()) {
						row_talendLogs_LOGS.type = lcm.getType();
						row_talendLogs_LOGS.origin = (lcm.getOrigin() == null
								|| lcm.getOrigin().length() < 1 ? null : lcm
								.getOrigin());
						row_talendLogs_LOGS.priority = lcm.getPriority();
						row_talendLogs_LOGS.message = lcm.getMessage();
						row_talendLogs_LOGS.code = lcm.getCode();

						row_talendLogs_LOGS.moment = java.util.Calendar
								.getInstance().getTime();

						row_talendLogs_LOGS.pid = pid;
						row_talendLogs_LOGS.root_pid = rootPid;
						row_talendLogs_LOGS.father_pid = fatherPid;

						row_talendLogs_LOGS.project = projectName;
						row_talendLogs_LOGS.job = jobName;
						row_talendLogs_LOGS.context = contextStr;

						/**
						 * [talendLogs_LOGS begin ] stop
						 */

						/**
						 * [talendLogs_LOGS main ] start
						 */

						currentVirtualComponent = "talendLogs_LOGS";

						currentComponent = "talendLogs_LOGS";

						tos_count_talendLogs_LOGS++;

						/**
						 * [talendLogs_LOGS main ] stop
						 */

						/**
						 * [talendLogs_LOGS process_data_begin ] start
						 */

						currentVirtualComponent = "talendLogs_LOGS";

						currentComponent = "talendLogs_LOGS";

						/**
						 * [talendLogs_LOGS process_data_begin ] stop
						 */

						/**
						 * [talendLogs_FILE main ] start
						 */

						currentVirtualComponent = "talendLogs_FILE";

						currentComponent = "talendLogs_FILE";

						// Main
						// row_talendLogs_LOGS

						if (execStat) {
							runStat.updateStatOnConnection("Main" + iterateId,
									1, 1);
						}

						StringBuilder sb_talendLogs_FILE = new StringBuilder();
						if (row_talendLogs_LOGS.moment != null) {
							sb_talendLogs_FILE.append(FormatterUtils
									.format_Date(row_talendLogs_LOGS.moment,
											"yyyy-MM-dd HH:mm:ss"));
						}
						sb_talendLogs_FILE.append(OUT_DELIM_talendLogs_FILE);
						if (row_talendLogs_LOGS.pid != null) {
							sb_talendLogs_FILE.append(row_talendLogs_LOGS.pid);
						}
						sb_talendLogs_FILE.append(OUT_DELIM_talendLogs_FILE);
						if (row_talendLogs_LOGS.root_pid != null) {
							sb_talendLogs_FILE
									.append(row_talendLogs_LOGS.root_pid);
						}
						sb_talendLogs_FILE.append(OUT_DELIM_talendLogs_FILE);
						if (row_talendLogs_LOGS.father_pid != null) {
							sb_talendLogs_FILE
									.append(row_talendLogs_LOGS.father_pid);
						}
						sb_talendLogs_FILE.append(OUT_DELIM_talendLogs_FILE);
						if (row_talendLogs_LOGS.project != null) {
							sb_talendLogs_FILE
									.append(row_talendLogs_LOGS.project);
						}
						sb_talendLogs_FILE.append(OUT_DELIM_talendLogs_FILE);
						if (row_talendLogs_LOGS.job != null) {
							sb_talendLogs_FILE.append(row_talendLogs_LOGS.job);
						}
						sb_talendLogs_FILE.append(OUT_DELIM_talendLogs_FILE);
						if (row_talendLogs_LOGS.context != null) {
							sb_talendLogs_FILE
									.append(row_talendLogs_LOGS.context);
						}
						sb_talendLogs_FILE.append(OUT_DELIM_talendLogs_FILE);
						if (row_talendLogs_LOGS.priority != null) {
							sb_talendLogs_FILE
									.append(row_talendLogs_LOGS.priority);
						}
						sb_talendLogs_FILE.append(OUT_DELIM_talendLogs_FILE);
						if (row_talendLogs_LOGS.type != null) {
							sb_talendLogs_FILE.append(row_talendLogs_LOGS.type);
						}
						sb_talendLogs_FILE.append(OUT_DELIM_talendLogs_FILE);
						if (row_talendLogs_LOGS.origin != null) {
							sb_talendLogs_FILE
									.append(row_talendLogs_LOGS.origin);
						}
						sb_talendLogs_FILE.append(OUT_DELIM_talendLogs_FILE);
						if (row_talendLogs_LOGS.message != null) {
							sb_talendLogs_FILE
									.append(row_talendLogs_LOGS.message);
						}
						sb_talendLogs_FILE.append(OUT_DELIM_talendLogs_FILE);
						if (row_talendLogs_LOGS.code != null) {
							sb_talendLogs_FILE.append(row_talendLogs_LOGS.code);
						}
						sb_talendLogs_FILE
								.append(OUT_DELIM_ROWSEP_talendLogs_FILE);

						synchronized (multiThreadLockWrite) {
							nb_line_talendLogs_FILE++;
							resourceMap.put("nb_line_talendLogs_FILE",
									nb_line_talendLogs_FILE);

							outtalendLogs_FILE.write(sb_talendLogs_FILE
									.toString());
							log.debug("talendLogs_FILE - Writing the record "
									+ nb_line_talendLogs_FILE + ".");

						}

						tos_count_talendLogs_FILE++;

						/**
						 * [talendLogs_FILE main ] stop
						 */

						/**
						 * [talendLogs_FILE process_data_begin ] start
						 */

						currentVirtualComponent = "talendLogs_FILE";

						currentComponent = "talendLogs_FILE";

						/**
						 * [talendLogs_FILE process_data_begin ] stop
						 */

						/**
						 * [talendLogs_FILE process_data_end ] start
						 */

						currentVirtualComponent = "talendLogs_FILE";

						currentComponent = "talendLogs_FILE";

						/**
						 * [talendLogs_FILE process_data_end ] stop
						 */

						/**
						 * [talendLogs_LOGS process_data_end ] start
						 */

						currentVirtualComponent = "talendLogs_LOGS";

						currentComponent = "talendLogs_LOGS";

						/**
						 * [talendLogs_LOGS process_data_end ] stop
						 */

						/**
						 * [talendLogs_LOGS end ] start
						 */

						currentVirtualComponent = "talendLogs_LOGS";

						currentComponent = "talendLogs_LOGS";

					}
				} catch (Exception e_talendLogs_LOGS) {
					logIgnoredError(
							String.format(
									"talendLogs_LOGS - tLogCatcher failed to process log message(s) due to internal error: %s",
									e_talendLogs_LOGS), e_talendLogs_LOGS);
				}

				if (log.isDebugEnabled())
					log.debug("talendLogs_LOGS - " + ("Done."));

				ok_Hash.put("talendLogs_LOGS", true);
				end_Hash.put("talendLogs_LOGS", System.currentTimeMillis());

				/**
				 * [talendLogs_LOGS end ] stop
				 */

				/**
				 * [talendLogs_FILE end ] start
				 */

				currentVirtualComponent = "talendLogs_FILE";

				currentComponent = "talendLogs_FILE";

				synchronized (multiThreadLockWrite) {

					if (outtalendLogs_FILE != null) {
						outtalendLogs_FILE.flush();
						outtalendLogs_FILE.close();
					}

					globalMap.put("talendLogs_FILE_NB_LINE",
							nb_line_talendLogs_FILE);
					globalMap.put("talendLogs_FILE_FILE_NAME",
							fileName_talendLogs_FILE);

				}

				resourceMap.put("finish_talendLogs_FILE", true);

				log.debug("talendLogs_FILE - Written records count: "
						+ nb_line_talendLogs_FILE + " .");

				if (execStat) {
					if (resourceMap.get("inIterateVComp") == null
							|| !((Boolean) resourceMap.get("inIterateVComp"))) {
						runStat.updateStatOnConnection("Main" + iterateId, 2, 0);
					}
				}

				if (log.isDebugEnabled())
					log.debug("talendLogs_FILE - " + ("Done."));

				ok_Hash.put("talendLogs_FILE", true);
				end_Hash.put("talendLogs_FILE", System.currentTimeMillis());

				/**
				 * [talendLogs_FILE end ] stop
				 */

			}// end the resume

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent,
					globalMap);

			te.setVirtualComponentName(currentVirtualComponent);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [talendLogs_LOGS finally ] start
				 */

				currentVirtualComponent = "talendLogs_LOGS";

				currentComponent = "talendLogs_LOGS";

				/**
				 * [talendLogs_LOGS finally ] stop
				 */

				/**
				 * [talendLogs_FILE finally ] start
				 */

				currentVirtualComponent = "talendLogs_FILE";

				currentComponent = "talendLogs_FILE";

				if (resourceMap.get("finish_talendLogs_FILE") == null) {

					synchronized (multiThreadLockWrite) {

						java.io.Writer outtalendLogs_FILE = (java.io.Writer) resourceMap
								.get("out_talendLogs_FILE");
						if (outtalendLogs_FILE != null) {
							outtalendLogs_FILE.flush();
							outtalendLogs_FILE.close();
						}

					}

				}

				/**
				 * [talendLogs_FILE finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("talendLogs_LOGS_SUBPROCESS_STATE", 1);
	}

	public static class row_talendMeter_METTERStruct implements
			routines.system.IPersistableRow<row_talendMeter_METTERStruct> {
		final static byte[] commonByteArrayLock_DEMOTALEND_BigFiles = new byte[0];
		static byte[] commonByteArray_DEMOTALEND_BigFiles = new byte[0];

		public java.util.Date moment;

		public java.util.Date getMoment() {
			return this.moment;
		}

		public String pid;

		public String getPid() {
			return this.pid;
		}

		public String father_pid;

		public String getFather_pid() {
			return this.father_pid;
		}

		public String root_pid;

		public String getRoot_pid() {
			return this.root_pid;
		}

		public Long system_pid;

		public Long getSystem_pid() {
			return this.system_pid;
		}

		public String project;

		public String getProject() {
			return this.project;
		}

		public String job;

		public String getJob() {
			return this.job;
		}

		public String job_repository_id;

		public String getJob_repository_id() {
			return this.job_repository_id;
		}

		public String job_version;

		public String getJob_version() {
			return this.job_version;
		}

		public String context;

		public String getContext() {
			return this.context;
		}

		public String origin;

		public String getOrigin() {
			return this.origin;
		}

		public String label;

		public String getLabel() {
			return this.label;
		}

		public Integer count;

		public Integer getCount() {
			return this.count;
		}

		public Integer reference;

		public Integer getReference() {
			return this.reference;
		}

		public String thresholds;

		public String getThresholds() {
			return this.thresholds;
		}

		private java.util.Date readDate(ObjectInputStream dis)
				throws IOException {
			java.util.Date dateReturn = null;
			int length = 0;
			length = dis.readByte();
			if (length == -1) {
				dateReturn = null;
			} else {
				dateReturn = new Date(dis.readLong());
			}
			return dateReturn;
		}

		private void writeDate(java.util.Date date1, ObjectOutputStream dos)
				throws IOException {
			if (date1 == null) {
				dos.writeByte(-1);
			} else {
				dos.writeByte(0);
				dos.writeLong(date1.getTime());
			}
		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_DEMOTALEND_BigFiles.length) {
					if (length < 1024
							&& commonByteArray_DEMOTALEND_BigFiles.length == 0) {
						commonByteArray_DEMOTALEND_BigFiles = new byte[1024];
					} else {
						commonByteArray_DEMOTALEND_BigFiles = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_DEMOTALEND_BigFiles, 0, length);
				strReturn = new String(commonByteArray_DEMOTALEND_BigFiles, 0,
						length, utf8Charset);
			}
			return strReturn;
		}

		private void writeString(String str, ObjectOutputStream dos)
				throws IOException {
			if (str == null) {
				dos.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				dos.writeInt(byteArray.length);
				dos.write(byteArray);
			}
		}

		private Integer readInteger(ObjectInputStream dis) throws IOException {
			Integer intReturn;
			int length = 0;
			length = dis.readByte();
			if (length == -1) {
				intReturn = null;
			} else {
				intReturn = dis.readInt();
			}
			return intReturn;
		}

		private void writeInteger(Integer intNum, ObjectOutputStream dos)
				throws IOException {
			if (intNum == null) {
				dos.writeByte(-1);
			} else {
				dos.writeByte(0);
				dos.writeInt(intNum);
			}
		}

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_DEMOTALEND_BigFiles) {

				try {

					int length = 0;

					this.moment = readDate(dis);

					this.pid = readString(dis);

					this.father_pid = readString(dis);

					this.root_pid = readString(dis);

					length = dis.readByte();
					if (length == -1) {
						this.system_pid = null;
					} else {
						this.system_pid = dis.readLong();
					}

					this.project = readString(dis);

					this.job = readString(dis);

					this.job_repository_id = readString(dis);

					this.job_version = readString(dis);

					this.context = readString(dis);

					this.origin = readString(dis);

					this.label = readString(dis);

					this.count = readInteger(dis);

					this.reference = readInteger(dis);

					this.thresholds = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// java.util.Date

				writeDate(this.moment, dos);

				// String

				writeString(this.pid, dos);

				// String

				writeString(this.father_pid, dos);

				// String

				writeString(this.root_pid, dos);

				// Long

				if (this.system_pid == null) {
					dos.writeByte(-1);
				} else {
					dos.writeByte(0);
					dos.writeLong(this.system_pid);
				}

				// String

				writeString(this.project, dos);

				// String

				writeString(this.job, dos);

				// String

				writeString(this.job_repository_id, dos);

				// String

				writeString(this.job_version, dos);

				// String

				writeString(this.context, dos);

				// String

				writeString(this.origin, dos);

				// String

				writeString(this.label, dos);

				// Integer

				writeInteger(this.count, dos);

				// Integer

				writeInteger(this.reference, dos);

				// String

				writeString(this.thresholds, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("moment=" + String.valueOf(moment));
			sb.append(",pid=" + pid);
			sb.append(",father_pid=" + father_pid);
			sb.append(",root_pid=" + root_pid);
			sb.append(",system_pid=" + String.valueOf(system_pid));
			sb.append(",project=" + project);
			sb.append(",job=" + job);
			sb.append(",job_repository_id=" + job_repository_id);
			sb.append(",job_version=" + job_version);
			sb.append(",context=" + context);
			sb.append(",origin=" + origin);
			sb.append(",label=" + label);
			sb.append(",count=" + String.valueOf(count));
			sb.append(",reference=" + String.valueOf(reference));
			sb.append(",thresholds=" + thresholds);
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (moment == null) {
				sb.append("<null>");
			} else {
				sb.append(moment);
			}

			sb.append("|");

			if (pid == null) {
				sb.append("<null>");
			} else {
				sb.append(pid);
			}

			sb.append("|");

			if (father_pid == null) {
				sb.append("<null>");
			} else {
				sb.append(father_pid);
			}

			sb.append("|");

			if (root_pid == null) {
				sb.append("<null>");
			} else {
				sb.append(root_pid);
			}

			sb.append("|");

			if (system_pid == null) {
				sb.append("<null>");
			} else {
				sb.append(system_pid);
			}

			sb.append("|");

			if (project == null) {
				sb.append("<null>");
			} else {
				sb.append(project);
			}

			sb.append("|");

			if (job == null) {
				sb.append("<null>");
			} else {
				sb.append(job);
			}

			sb.append("|");

			if (job_repository_id == null) {
				sb.append("<null>");
			} else {
				sb.append(job_repository_id);
			}

			sb.append("|");

			if (job_version == null) {
				sb.append("<null>");
			} else {
				sb.append(job_version);
			}

			sb.append("|");

			if (context == null) {
				sb.append("<null>");
			} else {
				sb.append(context);
			}

			sb.append("|");

			if (origin == null) {
				sb.append("<null>");
			} else {
				sb.append(origin);
			}

			sb.append("|");

			if (label == null) {
				sb.append("<null>");
			} else {
				sb.append(label);
			}

			sb.append("|");

			if (count == null) {
				sb.append("<null>");
			} else {
				sb.append(count);
			}

			sb.append("|");

			if (reference == null) {
				sb.append("<null>");
			} else {
				sb.append(reference);
			}

			sb.append("|");

			if (thresholds == null) {
				sb.append("<null>");
			} else {
				sb.append(thresholds);
			}

			sb.append("|");

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(row_talendMeter_METTERStruct other) {

			int returnValue = -1;

			return returnValue;
		}

		private int checkNullsAndCompare(Object object1, Object object2) {
			int returnValue = 0;
			if (object1 instanceof Comparable && object2 instanceof Comparable) {
				returnValue = ((Comparable) object1).compareTo(object2);
			} else if (object1 != null && object2 != null) {
				returnValue = compareStrings(object1.toString(),
						object2.toString());
			} else if (object1 == null && object2 != null) {
				returnValue = 1;
			} else if (object1 != null && object2 == null) {
				returnValue = -1;
			} else {
				returnValue = 0;
			}

			return returnValue;
		}

		private int compareStrings(String string1, String string2) {
			return string1.compareTo(string2);
		}

	}

	public void talendMeter_METTERProcess(
			final java.util.Map<String, Object> globalMap)
			throws TalendException {
		globalMap.put("talendMeter_METTER_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;
		String currentVirtualComponent = null;

		String iterateId = "";

		String currentComponent = "";
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception()
						.getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { // start the resume
				globalResumeTicket = true;

				row_talendMeter_METTERStruct row_talendMeter_METTER = new row_talendMeter_METTERStruct();

				/**
				 * [talendMeter_FILE begin ] start
				 */

				ok_Hash.put("talendMeter_FILE", false);
				start_Hash.put("talendMeter_FILE", System.currentTimeMillis());

				currentVirtualComponent = "talendMeter_FILE";

				currentComponent = "talendMeter_FILE";

				if (execStat) {
					if (resourceMap.get("inIterateVComp") == null) {

						runStat.updateStatOnConnection("Main" + iterateId, 0, 0);

					}
				}

				int tos_count_talendMeter_FILE = 0;

				if (log.isDebugEnabled())
					log.debug("talendMeter_FILE - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_talendMeter_FILE {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_talendMeter_FILE = new StringBuilder();
							log4jParamters_talendMeter_FILE
									.append("Parameters:");
							log4jParamters_talendMeter_FILE.append("USESTREAM"
									+ " = " + "false");
							log4jParamters_talendMeter_FILE.append(" | ");
							log4jParamters_talendMeter_FILE
									.append("FILENAME"
											+ " = "
											+ "\"C:/Talend-Studio-20181026_1147-V7.1.1/Talend-Studio-20181026_1147-V7.1.1/workspace/.metadata\"+ \"/\" +\"meter_file.txt\"");
							log4jParamters_talendMeter_FILE.append(" | ");
							log4jParamters_talendMeter_FILE
									.append("ROWSEPARATOR" + " = " + "\"\\n\"");
							log4jParamters_talendMeter_FILE.append(" | ");
							log4jParamters_talendMeter_FILE
									.append("FIELDSEPARATOR" + " = " + "\";\"");
							log4jParamters_talendMeter_FILE.append(" | ");
							log4jParamters_talendMeter_FILE.append("APPEND"
									+ " = " + "true");
							log4jParamters_talendMeter_FILE.append(" | ");
							log4jParamters_talendMeter_FILE
									.append("INCLUDEHEADER" + " = " + "false");
							log4jParamters_talendMeter_FILE.append(" | ");
							log4jParamters_talendMeter_FILE
									.append("ADVANCED_SEPARATOR" + " = "
											+ "false");
							log4jParamters_talendMeter_FILE.append(" | ");
							log4jParamters_talendMeter_FILE.append("CSV_OPTION"
									+ " = " + "false");
							log4jParamters_talendMeter_FILE.append(" | ");
							log4jParamters_talendMeter_FILE.append("CREATE"
									+ " = " + "true");
							log4jParamters_talendMeter_FILE.append(" | ");
							log4jParamters_talendMeter_FILE.append("SPLIT"
									+ " = " + "false");
							log4jParamters_talendMeter_FILE.append(" | ");
							log4jParamters_talendMeter_FILE.append("FLUSHONROW"
									+ " = " + "false");
							log4jParamters_talendMeter_FILE.append(" | ");
							log4jParamters_talendMeter_FILE.append("ROW_MODE"
									+ " = " + "false");
							log4jParamters_talendMeter_FILE.append(" | ");
							log4jParamters_talendMeter_FILE.append("ENCODING"
									+ " = " + "\"ISO-8859-15\"");
							log4jParamters_talendMeter_FILE.append(" | ");
							log4jParamters_talendMeter_FILE
									.append("DELETE_EMPTYFILE" + " = "
											+ "false");
							log4jParamters_talendMeter_FILE.append(" | ");
							if (log.isDebugEnabled())
								log.debug("talendMeter_FILE - "
										+ (log4jParamters_talendMeter_FILE));
						}
					}
					new BytesLimit65535_talendMeter_FILE().limitLog4jByte();
				}

				String fileName_talendMeter_FILE = "";
				fileName_talendMeter_FILE = (new java.io.File(
						"C:/Talend-Studio-20181026_1147-V7.1.1/Talend-Studio-20181026_1147-V7.1.1/workspace/.metadata"
								+ "/" + "meter_file.txt")).getAbsolutePath()
						.replace("\\", "/");
				String fullName_talendMeter_FILE = null;
				String extension_talendMeter_FILE = null;
				String directory_talendMeter_FILE = null;
				if ((fileName_talendMeter_FILE.indexOf("/") != -1)) {
					if (fileName_talendMeter_FILE.lastIndexOf(".") < fileName_talendMeter_FILE
							.lastIndexOf("/")) {
						fullName_talendMeter_FILE = fileName_talendMeter_FILE;
						extension_talendMeter_FILE = "";
					} else {
						fullName_talendMeter_FILE = fileName_talendMeter_FILE
								.substring(0, fileName_talendMeter_FILE
										.lastIndexOf("."));
						extension_talendMeter_FILE = fileName_talendMeter_FILE
								.substring(fileName_talendMeter_FILE
										.lastIndexOf("."));
					}
					directory_talendMeter_FILE = fileName_talendMeter_FILE
							.substring(0,
									fileName_talendMeter_FILE.lastIndexOf("/"));
				} else {
					if (fileName_talendMeter_FILE.lastIndexOf(".") != -1) {
						fullName_talendMeter_FILE = fileName_talendMeter_FILE
								.substring(0, fileName_talendMeter_FILE
										.lastIndexOf("."));
						extension_talendMeter_FILE = fileName_talendMeter_FILE
								.substring(fileName_talendMeter_FILE
										.lastIndexOf("."));
					} else {
						fullName_talendMeter_FILE = fileName_talendMeter_FILE;
						extension_talendMeter_FILE = "";
					}
					directory_talendMeter_FILE = "";
				}
				boolean isFileGenerated_talendMeter_FILE = true;
				java.io.File filetalendMeter_FILE = new java.io.File(
						fileName_talendMeter_FILE);
				globalMap.put("talendMeter_FILE_FILE_NAME",
						fileName_talendMeter_FILE);
				if (filetalendMeter_FILE.exists()) {
					isFileGenerated_talendMeter_FILE = false;
				}
				int nb_line_talendMeter_FILE = 0;
				int splitedFileNo_talendMeter_FILE = 0;
				int currentRow_talendMeter_FILE = 0;

				final String OUT_DELIM_talendMeter_FILE = /**
				 * Start field
				 * talendMeter_FILE:FIELDSEPARATOR
				 */
				";"/** End field talendMeter_FILE:FIELDSEPARATOR */
				;

				final String OUT_DELIM_ROWSEP_talendMeter_FILE = /**
				 * Start field
				 * talendMeter_FILE:ROWSEPARATOR
				 */
				"\n"/** End field talendMeter_FILE:ROWSEPARATOR */
				;

				// create directory only if not exists
				if (directory_talendMeter_FILE != null
						&& directory_talendMeter_FILE.trim().length() != 0) {
					java.io.File dir_talendMeter_FILE = new java.io.File(
							directory_talendMeter_FILE);
					if (!dir_talendMeter_FILE.exists()) {
						log.info("talendMeter_FILE - Creating directory '"
								+ dir_talendMeter_FILE.getCanonicalPath()
								+ "'.");
						dir_talendMeter_FILE.mkdirs();
						log.info("talendMeter_FILE - The directory '"
								+ dir_talendMeter_FILE.getCanonicalPath()
								+ "' has been created successfully.");
					}
				}

				// routines.system.Row
				java.io.Writer outtalendMeter_FILE = null;

				outtalendMeter_FILE = new java.io.BufferedWriter(
						new java.io.OutputStreamWriter(
								new java.io.FileOutputStream(
										fileName_talendMeter_FILE, true),
								"ISO-8859-15"));

				resourceMap.put("out_talendMeter_FILE", outtalendMeter_FILE);
				resourceMap.put("nb_line_talendMeter_FILE",
						nb_line_talendMeter_FILE);

				/**
				 * [talendMeter_FILE begin ] stop
				 */

				/**
				 * [talendMeter_METTER begin ] start
				 */

				ok_Hash.put("talendMeter_METTER", false);
				start_Hash
						.put("talendMeter_METTER", System.currentTimeMillis());

				currentVirtualComponent = "talendMeter_METTER";

				currentComponent = "talendMeter_METTER";

				int tos_count_talendMeter_METTER = 0;

				if (log.isDebugEnabled())
					log.debug("talendMeter_METTER - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_talendMeter_METTER {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_talendMeter_METTER = new StringBuilder();
							log4jParamters_talendMeter_METTER
									.append("Parameters:");
							if (log.isDebugEnabled())
								log.debug("talendMeter_METTER - "
										+ (log4jParamters_talendMeter_METTER));
						}
					}
					new BytesLimit65535_talendMeter_METTER().limitLog4jByte();
				}

				for (MetterCatcherUtils.MetterCatcherMessage mcm : talendMeter_METTER
						.getMessages()) {
					row_talendMeter_METTER.pid = pid;
					row_talendMeter_METTER.root_pid = rootPid;
					row_talendMeter_METTER.father_pid = fatherPid;
					row_talendMeter_METTER.project = projectName;
					row_talendMeter_METTER.job = jobName;
					row_talendMeter_METTER.context = contextStr;
					row_talendMeter_METTER.origin = (mcm.getOrigin() == null
							|| mcm.getOrigin().length() < 1 ? null : mcm
							.getOrigin());
					row_talendMeter_METTER.moment = mcm.getMoment();
					row_talendMeter_METTER.job_version = mcm.getJobVersion();
					row_talendMeter_METTER.job_repository_id = mcm.getJobId();
					row_talendMeter_METTER.system_pid = mcm.getSystemPid();
					row_talendMeter_METTER.label = mcm.getLabel();
					row_talendMeter_METTER.count = mcm.getCount();
					row_talendMeter_METTER.reference = talendMeter_METTER
							.getConnLinesCount(mcm.getReferense() + "_count");
					row_talendMeter_METTER.thresholds = mcm.getThresholds();

					/**
					 * [talendMeter_METTER begin ] stop
					 */

					/**
					 * [talendMeter_METTER main ] start
					 */

					currentVirtualComponent = "talendMeter_METTER";

					currentComponent = "talendMeter_METTER";

					tos_count_talendMeter_METTER++;

					/**
					 * [talendMeter_METTER main ] stop
					 */

					/**
					 * [talendMeter_METTER process_data_begin ] start
					 */

					currentVirtualComponent = "talendMeter_METTER";

					currentComponent = "talendMeter_METTER";

					/**
					 * [talendMeter_METTER process_data_begin ] stop
					 */

					/**
					 * [talendMeter_FILE main ] start
					 */

					currentVirtualComponent = "talendMeter_FILE";

					currentComponent = "talendMeter_FILE";

					// Main
					// row_talendMeter_METTER

					if (execStat) {
						runStat.updateStatOnConnection("Main" + iterateId, 1, 1);
					}

					StringBuilder sb_talendMeter_FILE = new StringBuilder();
					if (row_talendMeter_METTER.moment != null) {
						sb_talendMeter_FILE.append(FormatterUtils.format_Date(
								row_talendMeter_METTER.moment,
								"yyyy-MM-dd HH:mm:ss"));
					}
					sb_talendMeter_FILE.append(OUT_DELIM_talendMeter_FILE);
					if (row_talendMeter_METTER.pid != null) {
						sb_talendMeter_FILE.append(row_talendMeter_METTER.pid);
					}
					sb_talendMeter_FILE.append(OUT_DELIM_talendMeter_FILE);
					if (row_talendMeter_METTER.father_pid != null) {
						sb_talendMeter_FILE
								.append(row_talendMeter_METTER.father_pid);
					}
					sb_talendMeter_FILE.append(OUT_DELIM_talendMeter_FILE);
					if (row_talendMeter_METTER.root_pid != null) {
						sb_talendMeter_FILE
								.append(row_talendMeter_METTER.root_pid);
					}
					sb_talendMeter_FILE.append(OUT_DELIM_talendMeter_FILE);
					if (row_talendMeter_METTER.system_pid != null) {
						sb_talendMeter_FILE
								.append(row_talendMeter_METTER.system_pid);
					}
					sb_talendMeter_FILE.append(OUT_DELIM_talendMeter_FILE);
					if (row_talendMeter_METTER.project != null) {
						sb_talendMeter_FILE
								.append(row_talendMeter_METTER.project);
					}
					sb_talendMeter_FILE.append(OUT_DELIM_talendMeter_FILE);
					if (row_talendMeter_METTER.job != null) {
						sb_talendMeter_FILE.append(row_talendMeter_METTER.job);
					}
					sb_talendMeter_FILE.append(OUT_DELIM_talendMeter_FILE);
					if (row_talendMeter_METTER.job_repository_id != null) {
						sb_talendMeter_FILE
								.append(row_talendMeter_METTER.job_repository_id);
					}
					sb_talendMeter_FILE.append(OUT_DELIM_talendMeter_FILE);
					if (row_talendMeter_METTER.job_version != null) {
						sb_talendMeter_FILE
								.append(row_talendMeter_METTER.job_version);
					}
					sb_talendMeter_FILE.append(OUT_DELIM_talendMeter_FILE);
					if (row_talendMeter_METTER.context != null) {
						sb_talendMeter_FILE
								.append(row_talendMeter_METTER.context);
					}
					sb_talendMeter_FILE.append(OUT_DELIM_talendMeter_FILE);
					if (row_talendMeter_METTER.origin != null) {
						sb_talendMeter_FILE
								.append(row_talendMeter_METTER.origin);
					}
					sb_talendMeter_FILE.append(OUT_DELIM_talendMeter_FILE);
					if (row_talendMeter_METTER.label != null) {
						sb_talendMeter_FILE
								.append(row_talendMeter_METTER.label);
					}
					sb_talendMeter_FILE.append(OUT_DELIM_talendMeter_FILE);
					if (row_talendMeter_METTER.count != null) {
						sb_talendMeter_FILE
								.append(row_talendMeter_METTER.count);
					}
					sb_talendMeter_FILE.append(OUT_DELIM_talendMeter_FILE);
					if (row_talendMeter_METTER.reference != null) {
						sb_talendMeter_FILE
								.append(row_talendMeter_METTER.reference);
					}
					sb_talendMeter_FILE.append(OUT_DELIM_talendMeter_FILE);
					if (row_talendMeter_METTER.thresholds != null) {
						sb_talendMeter_FILE
								.append(row_talendMeter_METTER.thresholds);
					}
					sb_talendMeter_FILE
							.append(OUT_DELIM_ROWSEP_talendMeter_FILE);

					synchronized (multiThreadLockWrite) {
						nb_line_talendMeter_FILE++;
						resourceMap.put("nb_line_talendMeter_FILE",
								nb_line_talendMeter_FILE);

						outtalendMeter_FILE.write(sb_talendMeter_FILE
								.toString());
						log.debug("talendMeter_FILE - Writing the record "
								+ nb_line_talendMeter_FILE + ".");

					}

					tos_count_talendMeter_FILE++;

					/**
					 * [talendMeter_FILE main ] stop
					 */

					/**
					 * [talendMeter_FILE process_data_begin ] start
					 */

					currentVirtualComponent = "talendMeter_FILE";

					currentComponent = "talendMeter_FILE";

					/**
					 * [talendMeter_FILE process_data_begin ] stop
					 */

					/**
					 * [talendMeter_FILE process_data_end ] start
					 */

					currentVirtualComponent = "talendMeter_FILE";

					currentComponent = "talendMeter_FILE";

					/**
					 * [talendMeter_FILE process_data_end ] stop
					 */

					/**
					 * [talendMeter_METTER process_data_end ] start
					 */

					currentVirtualComponent = "talendMeter_METTER";

					currentComponent = "talendMeter_METTER";

					/**
					 * [talendMeter_METTER process_data_end ] stop
					 */

					/**
					 * [talendMeter_METTER end ] start
					 */

					currentVirtualComponent = "talendMeter_METTER";

					currentComponent = "talendMeter_METTER";

				}

				if (log.isDebugEnabled())
					log.debug("talendMeter_METTER - " + ("Done."));

				ok_Hash.put("talendMeter_METTER", true);
				end_Hash.put("talendMeter_METTER", System.currentTimeMillis());

				/**
				 * [talendMeter_METTER end ] stop
				 */

				/**
				 * [talendMeter_FILE end ] start
				 */

				currentVirtualComponent = "talendMeter_FILE";

				currentComponent = "talendMeter_FILE";

				synchronized (multiThreadLockWrite) {

					if (outtalendMeter_FILE != null) {
						outtalendMeter_FILE.flush();
						outtalendMeter_FILE.close();
					}

					globalMap.put("talendMeter_FILE_NB_LINE",
							nb_line_talendMeter_FILE);
					globalMap.put("talendMeter_FILE_FILE_NAME",
							fileName_talendMeter_FILE);

				}

				resourceMap.put("finish_talendMeter_FILE", true);

				log.debug("talendMeter_FILE - Written records count: "
						+ nb_line_talendMeter_FILE + " .");

				if (execStat) {
					if (resourceMap.get("inIterateVComp") == null
							|| !((Boolean) resourceMap.get("inIterateVComp"))) {
						runStat.updateStatOnConnection("Main" + iterateId, 2, 0);
					}
				}

				if (log.isDebugEnabled())
					log.debug("talendMeter_FILE - " + ("Done."));

				ok_Hash.put("talendMeter_FILE", true);
				end_Hash.put("talendMeter_FILE", System.currentTimeMillis());

				/**
				 * [talendMeter_FILE end ] stop
				 */

			}// end the resume

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent,
					globalMap);

			te.setVirtualComponentName(currentVirtualComponent);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [talendMeter_METTER finally ] start
				 */

				currentVirtualComponent = "talendMeter_METTER";

				currentComponent = "talendMeter_METTER";

				/**
				 * [talendMeter_METTER finally ] stop
				 */

				/**
				 * [talendMeter_FILE finally ] start
				 */

				currentVirtualComponent = "talendMeter_FILE";

				currentComponent = "talendMeter_FILE";

				if (resourceMap.get("finish_talendMeter_FILE") == null) {

					synchronized (multiThreadLockWrite) {

						java.io.Writer outtalendMeter_FILE = (java.io.Writer) resourceMap
								.get("out_talendMeter_FILE");
						if (outtalendMeter_FILE != null) {
							outtalendMeter_FILE.flush();
							outtalendMeter_FILE.close();
						}

					}

				}

				/**
				 * [talendMeter_FILE finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("talendMeter_METTER_SUBPROCESS_STATE", 1);
	}

	public String resuming_logs_dir_path = null;
	public String resuming_checkpoint_path = null;
	public String parent_part_launcher = null;
	private String resumeEntryMethodName = null;
	private boolean globalResumeTicket = false;

	public boolean watch = false;
	// portStats is null, it means don't execute the statistics
	public Integer portStats = null;
	public int portTraces = 4334;
	public String clientHost;
	public String defaultClientHost = "localhost";
	public String contextStr = "Default";
	public boolean isDefaultContext = true;
	public String pid = "0";
	public String rootPid = null;
	public String fatherPid = null;
	public String fatherNode = null;
	public long startTime = 0;
	public boolean isChildJob = false;
	public String log4jLevel = "";

	private boolean execStat = true;

	private ThreadLocal<java.util.Map<String, String>> threadLocal = new ThreadLocal<java.util.Map<String, String>>() {
		protected java.util.Map<String, String> initialValue() {
			java.util.Map<String, String> threadRunResultMap = new java.util.HashMap<String, String>();
			threadRunResultMap.put("errorCode", null);
			threadRunResultMap.put("status", "");
			return threadRunResultMap;
		};
	};

	private SyncInt runningThreadCount = new SyncInt();

	private class SyncInt {
		private int count = 0;

		public synchronized void add(int i) {
			count += i;
		}

		public synchronized int getCount() {
			return count;
		}
	}

	private PropertiesWithType context_param = new PropertiesWithType();
	public java.util.Map<String, Object> parentContextMap = new java.util.HashMap<String, Object>();

	public String status = "";

	public static void main(String[] args) {
		final BigFiles BigFilesClass = new BigFiles();

		int exitCode = BigFilesClass.runJobInTOS(args);
		if (exitCode == 0) {
			log.info("TalendJob: 'BigFiles' - Done.");
		}

		System.exit(exitCode);
	}

	public String[][] runJob(String[] args) {

		int exitCode = runJobInTOS(args);
		String[][] bufferValue = new String[][] { { Integer.toString(exitCode) } };

		return bufferValue;
	}

	public boolean hastBufferOutputComponent() {
		boolean hastBufferOutput = false;

		return hastBufferOutput;
	}

	public int runJobInTOS(String[] args) {
		// reset status
		status = "";

		String lastStr = "";
		for (String arg : args) {
			if (arg.equalsIgnoreCase("--context_param")) {
				lastStr = arg;
			} else if (lastStr.equals("")) {
				evalParam(arg);
			} else {
				evalParam(lastStr + " " + arg);
				lastStr = "";
			}
		}

		if (!"".equals(log4jLevel)) {
			if ("trace".equalsIgnoreCase(log4jLevel)) {
				log.setLevel(org.apache.log4j.Level.TRACE);
			} else if ("debug".equalsIgnoreCase(log4jLevel)) {
				log.setLevel(org.apache.log4j.Level.DEBUG);
			} else if ("info".equalsIgnoreCase(log4jLevel)) {
				log.setLevel(org.apache.log4j.Level.INFO);
			} else if ("warn".equalsIgnoreCase(log4jLevel)) {
				log.setLevel(org.apache.log4j.Level.WARN);
			} else if ("error".equalsIgnoreCase(log4jLevel)) {
				log.setLevel(org.apache.log4j.Level.ERROR);
			} else if ("fatal".equalsIgnoreCase(log4jLevel)) {
				log.setLevel(org.apache.log4j.Level.FATAL);
			} else if ("off".equalsIgnoreCase(log4jLevel)) {
				log.setLevel(org.apache.log4j.Level.OFF);
			}
			org.apache.log4j.Logger.getRootLogger().setLevel(log.getLevel());
		}
		log.info("TalendJob: 'BigFiles' - Start.");

		if (clientHost == null) {
			clientHost = defaultClientHost;
		}

		if (pid == null || "0".equals(pid)) {
			pid = TalendString.getAsciiRandomString(6);
		}

		if (rootPid == null) {
			rootPid = pid;
		}
		if (fatherPid == null) {
			fatherPid = pid;
		} else {
			isChildJob = true;
		}

		if (portStats != null) {
			// portStats = -1; //for testing
			if (portStats < 0 || portStats > 65535) {
				// issue:10869, the portStats is invalid, so this client socket
				// can't open
				System.err.println("The statistics socket port " + portStats
						+ " is invalid.");
				execStat = false;
			}
		} else {
			execStat = false;
		}

		try {
			// call job/subjob with an existing context, like:
			// --context=production. if without this parameter, there will use
			// the default context instead.
			java.io.InputStream inContext = BigFiles.class.getClassLoader()
					.getResourceAsStream(
							"demotalend/bigfiles_0_1/contexts/" + contextStr
									+ ".properties");
			if (inContext == null) {
				inContext = BigFiles.class
						.getClassLoader()
						.getResourceAsStream(
								"config/contexts/" + contextStr + ".properties");
			}
			if (inContext != null && context != null && context.isEmpty()) {
				// defaultProps is in order to keep the original context value
				defaultProps.load(inContext);
				inContext.close();
				context = new ContextProperties(defaultProps);
			} else if (!isDefaultContext) {
				// print info and job continue to run, for case: context_param
				// is not empty.
				System.err.println("Could not find the context " + contextStr);
			}

			if (!context_param.isEmpty()) {
				context.putAll(context_param);
				// set types for params from parentJobs
				for (Object key : context_param.keySet()) {
					String context_key = key.toString();
					String context_type = context_param
							.getContextType(context_key);
					context.setContextType(context_key, context_type);

				}
			}
		} catch (java.io.IOException ie) {
			System.err.println("Could not load context " + contextStr);
			ie.printStackTrace();
		}

		// get context value from parent directly
		if (parentContextMap != null && !parentContextMap.isEmpty()) {
		}

		// Resume: init the resumeUtil
		resumeEntryMethodName = ResumeUtil
				.getResumeEntryMethodName(resuming_checkpoint_path);
		resumeUtil = new ResumeUtil(resuming_logs_dir_path, isChildJob, rootPid);
		resumeUtil.initCommonInfo(pid, rootPid, fatherPid, projectName,
				jobName, contextStr, jobVersion);

		List<String> parametersToEncrypt = new java.util.ArrayList<String>();
		// Resume: jobStart
		resumeUtil.addLog("JOB_STARTED", "JOB:" + jobName,
				parent_part_launcher, Thread.currentThread().getId() + "", "",
				"", "", "",
				resumeUtil.convertToJsonText(context, parametersToEncrypt));

		if (execStat) {
			try {
				runStat.openSocket(!isChildJob);
				runStat.setAllPID(rootPid, fatherPid, pid, jobName);
				runStat.startThreadStat(clientHost, portStats);
				runStat.updateStatOnJob(RunStat.JOBSTART, fatherNode);
			} catch (java.io.IOException ioException) {
				ioException.printStackTrace();
			}
		}

		java.util.concurrent.ConcurrentHashMap<Object, Object> concurrentHashMap = new java.util.concurrent.ConcurrentHashMap<Object, Object>();
		globalMap.put("concurrentHashMap", concurrentHashMap);

		long startUsedMemory = Runtime.getRuntime().totalMemory()
				- Runtime.getRuntime().freeMemory();
		long endUsedMemory = 0;
		long end = 0;

		startTime = System.currentTimeMillis();
		talendStats_STATS.addMessage("begin");

		this.globalResumeTicket = true;// to run tPreJob

		try {
			talendStats_STATSProcess(globalMap);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		this.globalResumeTicket = false;// to run others jobs
		final Thread launchingThread = Thread.currentThread();
		runningThreadCount.add(1);
		new Thread() {
			public void run() {
				java.util.Map threadRunResultMap = new java.util.HashMap();
				threadRunResultMap.put("errorCode", null);
				threadRunResultMap.put("status", "");
				threadLocal.set(threadRunResultMap);

				try {
					((java.util.Map) threadLocal.get()).put("errorCode", null);
					tParallelize_1Process(globalMap);
					if (!"failure".equals(((java.util.Map) threadLocal.get())
							.get("status"))) {
						((java.util.Map) threadLocal.get())
								.put("status", "end");
					}
				} catch (TalendException e_tParallelize_1) {
					globalMap.put("tParallelize_1_SUBPROCESS_STATE", -1);

					e_tParallelize_1.printStackTrace();

				} catch (java.lang.Error e_tParallelize_1) {
					globalMap.put("tParallelize_1_SUBPROCESS_STATE", -1);
					((java.util.Map) threadLocal.get())
							.put("status", "failure");
					throw e_tParallelize_1;

				} finally {
					Integer localErrorCode = (Integer) (((java.util.Map) threadLocal
							.get()).get("errorCode"));
					String localStatus = (String) (((java.util.Map) threadLocal
							.get()).get("status"));
					if (localErrorCode != null) {
						if (errorCode == null
								|| localErrorCode.compareTo(errorCode) > 0) {
							errorCode = localErrorCode;
						}
					}
					if (!status.equals("failure")) {
						status = localStatus;
					}

					if ("true".equals(((java.util.Map) threadLocal.get())
							.get("JobInterrupted"))) {
						launchingThread.interrupt();
					}

					runningThreadCount.add(-1);
				}
			}
		}.start();

		boolean interrupted = false;
		while (runningThreadCount.getCount() > 0) {
			try {
				Thread.sleep(10);
			} catch (java.lang.InterruptedException e) {
				interrupted = true;
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}
		}

		if (interrupted) {
			Thread.currentThread().interrupt();
		}

		this.globalResumeTicket = true;// to run tPostJob

		end = System.currentTimeMillis();

		if (watch) {
			System.out.println((end - startTime) + " milliseconds");
		}

		endUsedMemory = Runtime.getRuntime().totalMemory()
				- Runtime.getRuntime().freeMemory();
		if (false) {
			System.out.println((endUsedMemory - startUsedMemory)
					+ " bytes memory increase when running : BigFiles");
		}
		talendStats_STATS.addMessage(status == "" ? "end" : status,
				(end - startTime));
		try {
			talendStats_STATSProcess(globalMap);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		if (execStat) {
			runStat.updateStatOnJob(RunStat.JOBEND, fatherNode);
			runStat.stopThreadStat();
		}
		int returnCode = 0;
		if (errorCode == null) {
			returnCode = status != null && status.equals("failure") ? 1 : 0;
		} else {
			returnCode = errorCode.intValue();
		}
		resumeUtil.addLog("JOB_ENDED", "JOB:" + jobName, parent_part_launcher,
				Thread.currentThread().getId() + "", "", "" + returnCode, "",
				"", "");

		return returnCode;

	}

	// only for OSGi env
	public void destroy() {

	}

	private java.util.Map<String, Object> getSharedConnections4REST() {
		java.util.Map<String, Object> connections = new java.util.HashMap<String, Object>();

		return connections;
	}

	private void evalParam(String arg) {
		if (arg.startsWith("--resuming_logs_dir_path")) {
			resuming_logs_dir_path = arg.substring(25);
		} else if (arg.startsWith("--resuming_checkpoint_path")) {
			resuming_checkpoint_path = arg.substring(27);
		} else if (arg.startsWith("--parent_part_launcher")) {
			parent_part_launcher = arg.substring(23);
		} else if (arg.startsWith("--watch")) {
			watch = true;
		} else if (arg.startsWith("--stat_port=")) {
			String portStatsStr = arg.substring(12);
			if (portStatsStr != null && !portStatsStr.equals("null")) {
				portStats = Integer.parseInt(portStatsStr);
			}
		} else if (arg.startsWith("--trace_port=")) {
			portTraces = Integer.parseInt(arg.substring(13));
		} else if (arg.startsWith("--client_host=")) {
			clientHost = arg.substring(14);
		} else if (arg.startsWith("--context=")) {
			contextStr = arg.substring(10);
			isDefaultContext = false;
		} else if (arg.startsWith("--father_pid=")) {
			fatherPid = arg.substring(13);
		} else if (arg.startsWith("--root_pid=")) {
			rootPid = arg.substring(11);
		} else if (arg.startsWith("--father_node=")) {
			fatherNode = arg.substring(14);
		} else if (arg.startsWith("--pid=")) {
			pid = arg.substring(6);
		} else if (arg.startsWith("--context_type")) {
			String keyValue = arg.substring(15);
			int index = -1;
			if (keyValue != null && (index = keyValue.indexOf('=')) > -1) {
				if (fatherPid == null) {
					context_param.setContextType(keyValue.substring(0, index),
							replaceEscapeChars(keyValue.substring(index + 1)));
				} else { // the subjob won't escape the especial chars
					context_param.setContextType(keyValue.substring(0, index),
							keyValue.substring(index + 1));
				}

			}

		} else if (arg.startsWith("--context_param")) {
			String keyValue = arg.substring(16);
			int index = -1;
			if (keyValue != null && (index = keyValue.indexOf('=')) > -1) {
				if (fatherPid == null) {
					context_param.put(keyValue.substring(0, index),
							replaceEscapeChars(keyValue.substring(index + 1)));
				} else { // the subjob won't escape the especial chars
					context_param.put(keyValue.substring(0, index),
							keyValue.substring(index + 1));
				}
			}
		} else if (arg.startsWith("--log4jLevel=")) {
			log4jLevel = arg.substring(13);
		}

	}

	private static final String NULL_VALUE_EXPRESSION_IN_COMMAND_STRING_FOR_CHILD_JOB_ONLY = "<TALEND_NULL>";

	private final String[][] escapeChars = { { "\\\\", "\\" }, { "\\n", "\n" },
			{ "\\'", "\'" }, { "\\r", "\r" }, { "\\f", "\f" }, { "\\b", "\b" },
			{ "\\t", "\t" } };

	private String replaceEscapeChars(String keyValue) {

		if (keyValue == null || ("").equals(keyValue.trim())) {
			return keyValue;
		}

		StringBuilder result = new StringBuilder();
		int currIndex = 0;
		while (currIndex < keyValue.length()) {
			int index = -1;
			// judege if the left string includes escape chars
			for (String[] strArray : escapeChars) {
				index = keyValue.indexOf(strArray[0], currIndex);
				if (index >= 0) {

					result.append(keyValue.substring(currIndex,
							index + strArray[0].length()).replace(strArray[0],
							strArray[1]));
					currIndex = index + strArray[0].length();
					break;
				}
			}
			// if the left string doesn't include escape chars, append the left
			// into the result
			if (index < 0) {
				result.append(keyValue.substring(currIndex));
				currIndex = currIndex + keyValue.length();
			}
		}

		return result.toString();
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public String getStatus() {
		return status;
	}

	ResumeUtil resumeUtil = null;
}
/************************************************************************************************
 * 174114 characters generated by Talend Cloud Real-Time Big Data Platform on
 * the 1 avril 2021 17:33:03 CEST
 ************************************************************************************************/
