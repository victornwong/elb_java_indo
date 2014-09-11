package org.victor;

public class IndonGlobalDefs
{
public static String BLANK_REPLACER = "------";

// 15/4/2010: branches job-folders prefix
public static String JOBFOLDERS_PREFIX = "ALSI";
public static String JB_JOBFOLDERS_PREFIX = "ALSI";
public static String KK_JOBFOLDERS_PREFIX = "ALSI";

// DB stuff -- 07/05/2012: use testbase/testdocumentstorage for ALSIndonesia setup - later need to change when loaded into rjkserver TODO
public static String MYSOFTDATABASESERVER = "192.168.0.5:1433";
public static String MYSOFTDATABASENAME = "indon_base";
public static String DOCUMENTSTORAGE_DATABASE = "indon_documentstorage";

public static String CHEMISTRY_RESULTS_TABLE = "elb_Chemistry_Results";
public static String JOBFOLDERS_TABLE = "JobFolders";
public static String JOBSAMPLES_TABLE =  "JobSamples";
public static String JOBTESTPARAMETERS_TABLE = "JobTestParameters";
public static String RUNLIST_TABLE = "RunList";
public static String RUNLISTITEMS_TABLE = "RunList_Items";
public static String CASHSALES_CUSTOMERINFO_TABLE = "CashSales_CustomerInfo";

// 24/08/2011: document management using a different database - AdminDocument. Tables struct same and with additional stuff
public static String DMS_DATABASE = "indon_AdminDocuments";
public static String ARCHIVE_DOC_DATABASE = "DocuArchives";

// END of DB Stuff

public static String FOLDERLOGGED = "LOGGED";
public static String FOLDERDRAFT = "DRAFT";
public static String FOLDERCOMMITED = "COMMITED";
public static String FOLDERRELEASED = "RELEASED";
public static String FOLDERWIP = "WIP";
public static String FOLDERRETEST = "RETEST";
	
}

