package org.victor;

import java.util.*;
import java.sql.*;
import groovy.sql.*;
import org.zkoss.zk.ui.*;
import org.victor.*;

public class SqlFuncs extends IndonGlobalDefs
{

public final Sql DMS_Sql()
{
	try
	{
		String dbstring = "jdbc:jtds:sqlserver://" + MYSOFTDATABASESERVER + "/" + DMS_DATABASE;
		return(Sql.newInstance(dbstring, "alsindonesia", "alsindonesia", "net.sourceforge.jtds.jdbc.Driver"));
	}
	catch (Exception e)
	{
		return null;
	}
}

public final Sql archivedocs_Sql()
{
	try
	{
		String dbstring = "jdbc:jtds:sqlserver://" + MYSOFTDATABASESERVER + "/" + ARCHIVE_DOC_DATABASE;
		return(Sql.newInstance(dbstring, "alsindonesia", "alsindonesia", "net.sourceforge.jtds.jdbc.Driver"));
	}
	catch (Exception e)
	{
		return null;
	}
}


/*
Open a JDBC to Mysoft database
Uses JTDS JDBC driver to access MS-SQL database and groovy.Sql
*/
public final Sql als_mysoftsql()
{
/*
driver = Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://alsslws007:1433/AccDatabase1", "sa", "sa");
Sql thesql = new Sql();
Sql retval = groovy.sql.Sql.newInstance(dbstring, "sa", "sa", "net.sourceforge.jtds.jdbc.Driver");
*/
	try
	{
		String dbstring = "jdbc:jtds:sqlserver://" + MYSOFTDATABASESERVER + "/" + MYSOFTDATABASENAME;
		return(Sql.newInstance(dbstring, "alsindonesia", "alsindonesia", "net.sourceforge.jtds.jdbc.Driver"));
	}
	catch (Exception e)
	{
		return null; //showMessageBox("Cannot access Mysoft database");
	}
}

// 14/4/2010: open JDBC connection to DocumentStorage database
public final Sql als_DocumentStorage()
{
	try
	{
		String dbstring = "jdbc:jtds:sqlserver://" + MYSOFTDATABASESERVER + "/" + DOCUMENTSTORAGE_DATABASE;
		return(Sql.newInstance(dbstring, "alsindonesia", "alsindonesia", "net.sourceforge.jtds.jdbc.Driver"));
	}
	catch (Exception e)
	{
		return null;
	}
}

// get company name based on ar_code passed
public final String getCompanyName(String tar_code)
{
	String retval = "-Undefined-";
	Sql sql = als_mysoftsql();
    if(sql == null) return retval;
	String sqlstatem = "select customer_name from customer where ar_code='" + tar_code + "'";
	GroovyRowResult therec = null;

	try
	{
		therec = (GroovyRowResult)sql.firstRow(sqlstatem);
		sql.close();
	}
	catch (java.sql.SQLException e) {}

	if(therec != null) retval = (String)therec.get("customer_name");
	return retval;
}

// get company customer record from mysoft.customer based on ar_code passed
public final Object getCompanyRecord(String tar_code)
{
	if(tar_code == null) return null;
	Sql sql = als_mysoftsql();
    if(sql == null) return null;
	String sqlstatem = "select * from customer where ar_code='" + tar_code + "'";
	GroovyRowResult therec = null;

	try
	{
		therec = (GroovyRowResult)sql.firstRow(sqlstatem);
		sql.close();
	}
	catch (java.sql.SQLException e) {}
	return therec;
}

public final GroovyRowResult getMySoftMasterProductRec(String iwhich)
{
	GroovyRowResult retval = null;
	Sql sql = als_mysoftsql();
	if(sql == null) return retval;
	String sqlstatem = "select * from stockmasterdetails where id=" + iwhich;

	try	
	{
		retval = (GroovyRowResult)sql.firstRow(sqlstatem);
		sql.close();
	}
	catch (java.sql.SQLException e) {}
	return retval;
}

// get a rec from equipment table
public final GroovyRowResult getEquipmentRec(String iorigid)
{
	GroovyRowResult retval = null;
	Sql sql = als_mysoftsql();
	if(sql == null) return retval;
	String sqlstat = "select * from Equipments where origid=" + iorigid;
	try
	{
		retval = (GroovyRowResult)sql.firstRow(sqlstat);
		sql.close();
	}
	catch (java.sql.SQLException e) {}
	return retval;
}

// Insert a rec into the Chemistry_Results table
public final void insertChemistryResult(String[] resarray)
{
	Sql sql = als_mysoftsql();
	if(sql == null) return;

	Connection thecon = sql.getConnection();

	// site_id = global_folderno	
	// samplecode = global_selected_sampleid

	// mod: 8/9/2010: change chemcode from analyte name to CAS
	// chemcode = CAS#

	// Result , Final = iresult (Final ain't doing any calc yet - just make them same)
	// Result_Unit = iunit
	// Method_Name = imethod
	// Analysed_Date = todaydate
	// EQID = ieqid
	// QA_Flag = iqaflag
	// username = useraccessobj.username
	// ResultStatus = "RESULT" // default for new entry
	// jobtestparameter_id = ijobtestparam_origid
	// reported = irepflag
	// mysoftcode = imysoftc

	// mod: 8/9/2010 - store originalchemname
	// originalchemname = ianalyte
	try
	{
		PreparedStatement pstmt = thecon.prepareStatement("insert into " + CHEMISTRY_RESULTS_TABLE + "(Site_ID, SampleCode, ChemCode, Result, Result_Unit, Final, " + 
		"Method_Name, Analysed_Date, EQID, QA_Flag, username, ResultStatus, jobtestparameter_id, reported, mysoftcode, deleted, OriginalChemName) values " + 
		"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

		pstmt.setString(1,resarray[0]);
		pstmt.setString(2,resarray[1]);
		pstmt.setString(3,resarray[2]);
		pstmt.setString(4,resarray[3]);
		pstmt.setString(5,resarray[4]);
		pstmt.setString(6,resarray[5]);
		pstmt.setString(7,resarray[6]);
		pstmt.setString(8,resarray[7]);

		pstmt.setString(9,resarray[8]);
		pstmt.setInt(10, Integer.parseInt(resarray[9]));
		pstmt.setString(11,resarray[10]);
		pstmt.setString(12,resarray[11]);
		pstmt.setInt(13, Integer.parseInt(resarray[12]));
		pstmt.setInt(14, Integer.parseInt(resarray[13]));
		pstmt.setInt(15, Integer.parseInt(resarray[14]));
		pstmt.setInt(16,0);
		pstmt.setString(17,resarray[15]);
		pstmt.executeUpdate();
		sql.close();
	}
	catch (java.sql.SQLException e) {}
}

// Knockoff from sqlfuncs.insertChemistryResult() to store results into elb_chemistryresults.formresults
// resarray[] :
// 0=jobfolder,1=sampleid,2=todaydate,3=username,4=jtporigid,5=mysoftcode,6=testname,7=method,8=results-ntext,9=formkeeper-id
public final boolean insertChemistryResult_Form(String[] resarray)
{
	Sql sql = als_mysoftsql();
	if(sql == null) return false;

	java.sql.Connection thecon = sql.getConnection();
	try
	{
		java.sql.PreparedStatement pstmt = thecon.prepareStatement("insert into elb_chemistry_results " +
		"(Site_ID, SampleCode, ChemCode, Result, Result_Unit, Final, " + 
		"Method_Name, Analysed_Date, EQID, QA_Flag, username, ResultStatus, jobtestparameter_id," + 
		"reported, mysoftcode, deleted, OriginalChemName,formresults,formkeeper_id) values " + 
		"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

		pstmt.setString(1,resarray[0]);
		pstmt.setString(2,resarray[1]);
		pstmt.setString(3,"");
		pstmt.setString(4,"FORM");
		pstmt.setString(5,"FORM");
		pstmt.setString(6,"FORM");
		pstmt.setString(7,resarray[7]);
		pstmt.setString(8,resarray[2]);

		pstmt.setString(9,"");
		pstmt.setInt(10, 0);
		pstmt.setString(11,resarray[3]);
		pstmt.setString(12,"RESULT");
		pstmt.setInt(13, Integer.parseInt(resarray[4]));
		pstmt.setInt(14, 1);
		pstmt.setInt(15, Integer.parseInt(resarray[5]));
		pstmt.setInt(16,0);
		pstmt.setString(17,resarray[6]);
		pstmt.setString(18,resarray[8]);
		pstmt.setString(19,resarray[9]);
		pstmt.executeUpdate();
		sql.close();
		return true;
	}
	catch (java.sql.SQLException e) { return false; }
}

// Update Chemistry_Results table
// resarray[] = ChemCode,Final,Result_Unit,QA_Flag,reported,Analysed_Date,origid(of chemistry_results)
public final void updateResultTrail(String[] resarray)
{
	Sql sql = als_mysoftsql();
	if(sql == null) return;
	Connection thecon = sql.getConnection();
	
	try
	{
		// 26/03/2012: modified below
		//PreparedStatement pstmt = thecon.prepareStatement("update " + CHEMISTRY_RESULTS_TABLE + " set ChemCode=? , Final=? , Result_Unit=? , QA_Flag=? , reported=? , Analysed_Date=? where origid=?");

		PreparedStatement pstmt = thecon.prepareStatement("update " + CHEMISTRY_RESULTS_TABLE + " set OriginalChemName=? , Final=? , Result_Unit=? , QA_Flag=? , reported=? , Analysed_Date=? where origid=?");

		pstmt.setString(1,resarray[0]);
		pstmt.setString(2,resarray[1]);
		pstmt.setString(3,resarray[2]);
		pstmt.setString(4,resarray[3]);
		pstmt.setInt(5,Integer.parseInt(resarray[4]));
		pstmt.setString(6, resarray[5]);
		pstmt.setInt(7,Integer.parseInt(resarray[6]));

		pstmt.executeUpdate();
		sql.close();
	}
	catch (java.sql.SQLException e) {}
}

public final GroovyRowResult getChemResult_Rec(String iorigid)
{
	GroovyRowResult retval = null;
	Sql sql = als_mysoftsql();
	if(sql == null) return retval;
	String sqlstm = "select * from " + CHEMISTRY_RESULTS_TABLE + " where origid=" + iorigid;

	try
	{
		retval = (GroovyRowResult)sql.firstRow(sqlstm);
		sql.close();
	}
	catch (java.sql.SQLException e) {}
	return retval;
}

// Database func: to get elb_Chemical_Results.final only, jtp_origid = JobTestParameters.origid
public final String getChemResult_Final(String jtp_origid)
{
	Sql sql = als_mysoftsql();
	if(sql == null) return null;
	// get only the latest final and also reported-flag set
	String sqlstm = "select top 1 Final from " + CHEMISTRY_RESULTS_TABLE + " where jobtestparameter_id=" + jtp_origid + " and reported=1 order by origid desc";
	GroovyRowResult therec = null;

	try
	{
		therec = (GroovyRowResult)sql.firstRow(sqlstm);
		sql.close();
	}
	catch (java.sql.SQLException e) {}

	String retval = "";
	if(therec != null) retval = (String)therec.get("Final");
	return retval;
}

// Database func: will return the latest result - order by origid desc
public final GroovyRowResult getLatestResult(String isampid, String imysoftc)
{
	GroovyRowResult retval = null;
	Sql sql = als_mysoftsql();
	if(sql == null) return retval;

	// select rec where reported=1 (need to report in COA) and deleted=0/null
	String sqlsta = "select top 1 * from " + CHEMISTRY_RESULTS_TABLE + " where SampleCode='" + isampid + "' and mysoftcode=" + imysoftc + 
	" and reported=1 and (deleted=0  or deleted is null) order by origid desc";

	try
	{
		retval = (GroovyRowResult)sql.firstRow(sqlsta);
		sql.close();
	}
	catch (java.sql.SQLException e) {}
	return retval;
}

// Useful database util func
// ifolderno = just the origid, not the whole string
// return false if number of results no equal to number of tests in samples
public final boolean checkForComplete_Results(String ifolderno)
{
	boolean retval = false;
	Sql sql = als_mysoftsql();
	if(sql == null) return retval;

	String sqlstm = "select jobsamples.origid as jsorigid, jobtestparameters.origid as jtporigid, " +
		"jobtestparameters.mysoftcode, elb_chemistry_results.chemcode from " +
		"jobsamples left join jobtestparameters " +
		"on jobsamples.origid = jobtestparameters.jobsamples_id " +
		"left join elb_chemistry_results " +
		"on elb_chemistry_results.mysoftcode = jobtestparameters.mysoftcode " +
		"where jobsamples.jobfolders_id=" + ifolderno +
		" and jobsamples.deleted = 0";

	List<GroovyRowResult> samprecs = null;
	
	try
	{
		samprecs = sql.rows(sqlstm);
		sql.close();
	}
	catch (java.sql.SQLException e) {}

	if(samprecs.size() > 0)
	{
		int mecount = 0;
		for(GroovyRowResult smrec : samprecs)
		{
			if(smrec.get("chemcode") != null) mecount++;
		}
		
		if(mecount == samprecs.size()) retval = true;
	}
	return retval;
}

// Database func: imagemap Mapper_Pos get a rec by origid
public final GroovyRowResult getMapperPos_Rec(String iorigid)
{
	GroovyRowResult retval = null;

	if(iorigid.equals("")) return retval;
	Sql sql = als_mysoftsql();
    if(sql == null) return retval;
	String sqlstm = "select * from Mapper_Pos where origid=" + iorigid;
	try
	{
		retval = (GroovyRowResult)sql.firstRow(sqlstm);
		sql.close();
	}
	catch (java.sql.SQLException e) {}
	return retval;
}

// Database func: add an audit-trail into elb_SystemAudit table
public final void addAuditTrail(String ilinkcode, String iaudit_notes, String iusername, String itodaydate)
{
	Generals kiboo = new Generals();
	Sql sql = als_mysoftsql();
	if(sql == null) return;

	ilinkcode = kiboo.replaceSingleQuotes(ilinkcode);
	iaudit_notes = kiboo.replaceSingleQuotes(iaudit_notes);

	String sqlstm = "insert into elb_SystemAudit (linking_code,audit_notes,username,datecreated,deleted) values " + 
	"('" + ilinkcode + "','" + iaudit_notes + "','" + iusername + "','" + itodaydate + "',0)";

	try
	{
		sql.execute(sqlstm);
		sql.close();
	}
	catch (java.sql.SQLException e) {}
}

// Database func: just toggle elb_SystemAudit.deleted flag
public final void toggleDelFlag_AuditTrail(String iorigid, String iwhat)
{
	Sql sql = als_mysoftsql();
	if(sql == null) return;
	String sqlstm = "update elb_SystemAudit set deleted=" + iwhat + " where origid=" + iorigid;
	try
	{
		sql.execute(sqlstm);
		sql.close();
	}
	catch (java.sql.SQLException e) {}
}

// Database func: get rec from customer_emails by origid
public final GroovyRowResult getCustomerEmails_Rec(String iorigid)
{
	GroovyRowResult retval = null;
	if(iorigid.equals("")) return retval;
	Sql sql = als_mysoftsql();
    if(sql == null) return retval;

	try
	{
		String sqlstm = "select * from customer_emails where origid=" + iorigid;
		retval = (GroovyRowResult)sql.firstRow(sqlstm);
		sql.close();
	}
	catch (java.sql.SQLException e) {}
	return retval;
}

// Database func: get rec from ZeroToleranceClients by origid
public final GroovyRowResult getZTC_Rec(String iorigid)
{
	GroovyRowResult retval = null;
	if(iorigid.equals("")) return retval;
	Sql sql = als_mysoftsql();
    if(sql == null) return retval;

	try
	{
		String sqlstm = "select * from zerotoleranceclients where origid=" + iorigid;
		retval = (GroovyRowResult)sql.firstRow(sqlstm);
		sql.close();
	}
	catch (java.sql.SQLException e) {}
	return retval;
}

public final void gpSqlExecuter(String isqlstm) throws SQLException
{
	Sql sql = als_mysoftsql();
	if(sql == null) return;
	sql.execute(isqlstm);
	sql.close();
}

public final ArrayList gpSqlGetRows(String isqlstm) throws SQLException
{
	Sql sql = als_mysoftsql();
	if(sql == null) return null;
	ArrayList retval = (ArrayList)sql.rows(isqlstm);
	sql.close();
	return retval;
}

public final GroovyRowResult gpSqlFirstRow(String isqlstm) throws SQLException
{
	Sql sql = als_mysoftsql();
	if(sql == null) return null;
	GroovyRowResult retval = (GroovyRowResult)sql.firstRow(isqlstm);
	sql.close();
	return retval;
}

public final String clobToString(Clob iwhat) throws SQLException
{
	if(iwhat == null) return "";
	String retval = iwhat.getSubString(1L,(int)iwhat.length());
	return retval;
}

public final GroovyRowResult getJobSchedule_Rec(String iorigid) throws SQLException
{
	Sql sql = als_mysoftsql();
	GroovyRowResult retval = null;
	if(sql == null ) return retval;
	String sqlstm = "select * from elb_jobschedules where origid=" + iorigid;
	retval = (GroovyRowResult)sql.firstRow(sqlstm);
	sql.close();
	return retval;
}

public final GroovyRowResult getChecklist_Rec(String iorigid) throws SQLException
{
	Sql sql = als_mysoftsql();
	GroovyRowResult retval = null;
	if(sql == null ) return retval;
	String sqlstm = "select * from elb_checklist where origid=" + iorigid;
	retval = (GroovyRowResult)sql.firstRow(sqlstm);
	sql.close();
	return retval;
}

public final GroovyRowResult getChecklistTemplate_Rec(String iorigid) throws SQLException
{
	Sql sql = als_mysoftsql();
	GroovyRowResult retval = null;
	if(sql == null ) return retval;
	String sqlstm = "select * from elb_checklist_templates where origid=" + iorigid;
	retval = (GroovyRowResult)sql.firstRow(sqlstm);
	sql.close();
	return retval;
}

public final GroovyRowResult getFormKeeper_rec(String iwhat) throws SQLException
{
	Sql sql = als_mysoftsql();
	GroovyRowResult retval = null;
	if(sql == null ) return retval;
	String sqlstm = "select * from elb_formkeeper where origid=" + iwhat;
	retval = (GroovyRowResult)sql.firstRow(sqlstm);
	sql.close();
	return retval;
}

}

