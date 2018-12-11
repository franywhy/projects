package com.izhubo.credit.vo;
/**
 * 结课后的学员实操作业分
 * @author 严志城
 * @Time 2017年3月6日16:37:53
 */
public class ClassEndingBusyworkVO{
		private String classid;
		private Double homework;
		/**
		 * 科目id
		 */
		private String subjectilesId;
		/**
		 * 学员id
		 */
		private String studentId;
		
		/**
		 * 是否合格
		 */
		private String def11;
		
		/**
		 * 获取科目主键
		 * @return
		 */
		public String getSubjectilesId() {
			return subjectilesId;
		}

		/**
		 * 设置科目主键
		 * @return
		 */
		public void setSubjectilesId(String subjectilesId) {
			this.subjectilesId = subjectilesId;
		}

		/**
		 * 获取学员主键
		 * @return
		 */
		public String getStudentId() {
			return studentId;
		}

		/**
		 * 设置学员主键
		 * @return
		 */
		public void setStudentId(String studentId) {
			this.studentId = studentId;
		}
		/**
		 * 获取班级主键
		 * @return
		 */
		public String getClassid() {
			return classid;
		}
		/**
		 * 设置班级主键
		 * @param classid
		 */
		public void setClassid(String classid) {
			this.classid = classid;
		}
		/**
		 * 获取作业分
		 * @return
		 */
		public Double getHomework() {
			return homework;
		}
		/**
		 * 设置作业分
		 * @param homework
		 */
		public void setHomework(Double homework) {
			this.homework = homework;
		}

		/**
		 * 获取是否合格
		 * @return
		 */
		public String getDef11() {
			return def11;
		}
		/**
		 * 设置是否合格
		 * @param def11
		 */
		public void setDef11(String def11) {
			this.def11 = def11;
		}
		
		
		
}
