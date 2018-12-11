/**
 * WebService1CallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.4  Built on : Oct 21, 2016 (10:47:34 BST)
 */
package com.izhubo.credit.webservice;


/**
 *  WebService1CallbackHandler Callback class, Users can extend this class and implement
 *  their own receiveResult and receiveError methods.
 */
public abstract class WebService1CallbackHandler {
    protected Object clientData;

    /**
     * User can pass in any object that needs to be accessed once the NonBlocking
     * Web service call is finished and appropriate method of this CallBack is called.
     * @param clientData Object mechanism by which the user can pass in user data
     * that will be avilable at the time this callback is called.
     */
    public WebService1CallbackHandler(Object clientData) {
        this.clientData = clientData;
    }

    /**
     * Please use this constructor if you don't want to set any clientData
     */
    public WebService1CallbackHandler() {
        this.clientData = null;
    }

    /**
     * Get the client data
     */
    public Object getClientData() {
        return clientData;
    }

    /**
     * auto generated Axis2 call back method for getStudentOfClassEndingExamComplianceRateByStudentNCCodeAndCourseCode method
     * override this method for handling normal response from getStudentOfClassEndingExamComplianceRateByStudentNCCodeAndCourseCode operation
     */
    public void receiveResultgetStudentOfClassEndingExamComplianceRateByStudentNCCodeAndCourseCode(
        com.izhubo.credit.webservice.WebService1Stub.GetStudentOfClassEndingExamComplianceRateByStudentNCCodeAndCourseCodeResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getStudentOfClassEndingExamComplianceRateByStudentNCCodeAndCourseCode operation
     */
    public void receiveErrorgetStudentOfClassEndingExamComplianceRateByStudentNCCodeAndCourseCode(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getUsers method
     * override this method for handling normal response from getUsers operation
     */
    public void receiveResultgetUsers(
        com.izhubo.credit.webservice.WebService1Stub.GetUsersResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getUsers operation
     */
    public void receiveErrorgetUsers(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getClassCompletionrateByClassCode method
     * override this method for handling normal response from getClassCompletionrateByClassCode operation
     */
    public void receiveResultgetClassCompletionrateByClassCode(
        com.izhubo.credit.webservice.WebService1Stub.GetClassCompletionrateByClassCodeResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getClassCompletionrateByClassCode operation
     */
    public void receiveErrorgetClassCompletionrateByClassCode(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getClassCompletionrateByClassCodeAndDate method
     * override this method for handling normal response from getClassCompletionrateByClassCodeAndDate operation
     */
    public void receiveResultgetClassCompletionrateByClassCodeAndDate(
        com.izhubo.credit.webservice.WebService1Stub.GetClassCompletionrateByClassCodeAndDateResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getClassCompletionrateByClassCodeAndDate operation
     */
    public void receiveErrorgetClassCompletionrateByClassCodeAndDate(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getStudentCompletionrateByUpdateTime method
     * override this method for handling normal response from getStudentCompletionrateByUpdateTime operation
     */
    public void receiveResultgetStudentCompletionrateByUpdateTime(
        com.izhubo.credit.webservice.WebService1Stub.GetStudentCompletionrateByUpdateTimeResponse result) {
    }

    
    public void receiveResultgetHomeWorkCompletion(
            com.izhubo.credit.webservice.WebService1Stub.GetHomeWorkCompletionResponse result) {
        }
    public void receiveErrorgetHomeWorkCompletion(
            java.lang.Exception e) {
        }
    
    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getStudentCompletionrateByUpdateTime operation
     */
    public void receiveErrorgetStudentCompletionrateByUpdateTime(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getETSBusinessOpportunityInfo_SyncToNCByTime method
     * override this method for handling normal response from getETSBusinessOpportunityInfo_SyncToNCByTime operation
     */
    public void receiveResultgetETSBusinessOpportunityInfo_SyncToNCByTime(
        com.izhubo.credit.webservice.WebService1Stub.GetETSBusinessOpportunityInfo_SyncToNCByTimeResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getETSBusinessOpportunityInfo_SyncToNCByTime operation
     */
    public void receiveErrorgetETSBusinessOpportunityInfo_SyncToNCByTime(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for upLoadSSOKey method
     * override this method for handling normal response from upLoadSSOKey operation
     */
    public void receiveResultupLoadSSOKey(
        com.izhubo.credit.webservice.WebService1Stub.UpLoadSSOKeyResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from upLoadSSOKey operation
     */
    public void receiveErrorupLoadSSOKey(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getStudentOfClassEndingTaskComplianceRateByTime method
     * override this method for handling normal response from getStudentOfClassEndingTaskComplianceRateByTime operation
     */
    public void receiveResultgetStudentOfClassEndingTaskComplianceRateByTime(
        com.izhubo.credit.webservice.WebService1Stub.GetStudentOfClassEndingTaskComplianceRateByTimeResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getStudentOfClassEndingTaskComplianceRateByTime operation
     */
    public void receiveErrorgetStudentOfClassEndingTaskComplianceRateByTime(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getKJSCExamDetailInfo_SyncToNCByTime method
     * override this method for handling normal response from getKJSCExamDetailInfo_SyncToNCByTime operation
     */
    public void receiveResultgetKJSCExamDetailInfo_SyncToNCByTime(
        com.izhubo.credit.webservice.WebService1Stub.GetKJSCExamDetailInfo_SyncToNCByTimeResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getKJSCExamDetailInfo_SyncToNCByTime operation
     */
    public void receiveErrorgetKJSCExamDetailInfo_SyncToNCByTime(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getClassCompletionrateByDateAndAreaCode method
     * override this method for handling normal response from getClassCompletionrateByDateAndAreaCode operation
     */
    public void receiveResultgetClassCompletionrateByDateAndAreaCode(
        com.izhubo.credit.webservice.WebService1Stub.GetClassCompletionrateByDateAndAreaCodeResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getClassCompletionrateByDateAndAreaCode operation
     */
    public void receiveErrorgetClassCompletionrateByDateAndAreaCode(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getClassJobStatisticsInfo_Sync_To_NCByTime method
     * override this method for handling normal response from getClassJobStatisticsInfo_Sync_To_NCByTime operation
     */
    public void receiveResultgetClassJobStatisticsInfo_Sync_To_NCByTime(
        com.izhubo.credit.webservice.WebService1Stub.GetClassJobStatisticsInfo_Sync_To_NCByTimeResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getClassJobStatisticsInfo_Sync_To_NCByTime operation
     */
    public void receiveErrorgetClassJobStatisticsInfo_Sync_To_NCByTime(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getStudentOfClassEndingExamComplianceRateByTime method
     * override this method for handling normal response from getStudentOfClassEndingExamComplianceRateByTime operation
     */
    public void receiveResultgetStudentOfClassEndingExamComplianceRateByTime(
        com.izhubo.credit.webservice.WebService1Stub.GetStudentOfClassEndingExamComplianceRateByTimeResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getStudentOfClassEndingExamComplianceRateByTime operation
     */
    public void receiveErrorgetStudentOfClassEndingExamComplianceRateByTime(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getClassJobStatisticsInfoInfo method
     * override this method for handling normal response from getClassJobStatisticsInfoInfo operation
     */
    public void receiveResultgetClassJobStatisticsInfoInfo(
        com.izhubo.credit.webservice.WebService1Stub.GetClassJobStatisticsInfoInfoResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getClassJobStatisticsInfoInfo operation
     */
    public void receiveErrorgetClassJobStatisticsInfoInfo(java.lang.Exception e) {
    }
}
