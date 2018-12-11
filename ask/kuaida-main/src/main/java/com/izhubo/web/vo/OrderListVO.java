package com.izhubo.web.vo;

import java.text.ParseException;

import com.izhubo.utils.DataUtils;
import com.izhubo.web.vo.OrderListVO.OrderListVOData;
import com.wordnik.swagger.annotations.ApiModelProperty;




public class OrderListVO extends BasePageResultVO<OrderListVOData> {
	

	public class OrderListVOData implements Comparable<OrderListVOData>{
		@ApiModelProperty(value = "_id")
		private String _id;

		@ApiModelProperty(value = "订单号")
		private String order_id;
		
		@ApiModelProperty(value = "商品名称")
		private String item_name;
		
		@ApiModelProperty(value = "商品图片地址")
		private String item_picurl;

		@ApiModelProperty(value = "订单时间")
		private Long create_time;
		
		
		@ApiModelProperty(value = "订单时间字符格式")
		private String create_time_text;
		
		
		@ApiModelProperty(value = "携带的商品id")
		private String item_id;
		
		@ApiModelProperty(value = "支付类型文字")
		private double pay_type;
		
		@ApiModelProperty(value = "订单状态  0未支付,1待支付,2支付成功")
		private String pay_status;
		
		@ApiModelProperty(value = "订单状态文字")
		private String pay_status_text;
		
		//private double actual_price;
		
		//private double discount_price;
		@ApiModelProperty(value = "user_id")
		private Integer user_id;

		@ApiModelProperty(value = "订单最终价格")
		private double price;
		
		/** 数据源头:1.订单只存在于恒企在线,NC中未生成报名表;2.NC报名报数据,恒企在线没有对应订单,属于线下报名;3.恒企在线存在订单和NC存在报名表,线上报名并已同步NC生成报名表 */
		@ApiModelProperty(value = "数据源头:1.订单只存在于恒企在线,NC中未生成报名表;2.NC报名报数据,恒企在线没有对应订单,属于线下报名;3.恒企在线存在订单和NC存在报名表,线上报名并已同步NC生成报名表")
		private double source;
		
		public String get_id() {
			return _id;
		}


		public void set_id(String _id) {
			this._id = _id;
		}


		public String getOrder_id() {
			return order_id;
		}


		public void setOrder_id(String order_id) {
			this.order_id = order_id;
		}


		public String getItem_name() {
			return item_name;
		}


		public void setItem_name(String item_name) {
			this.item_name = item_name;
		}


		public String getItem_picurl() {
			return item_picurl;
		}


		public void setItem_picurl(String item_picurl) {
			this.item_picurl = item_picurl;
		}


		public Long getCreate_time() {
			return create_time;
		}


		public void setCreate_time(Long create_time) {
			this.create_time = create_time;
		}


		public double getPay_type() {
			return pay_type;
		}


		public void setPay_type(double pay_type) {
			this.pay_type = pay_type;
		}


		public String getPay_status() {
			return pay_status;
		}


		public void setPay_status(String pay_status) {
			this.pay_status = pay_status;
		}


		public Integer getUser_id() {
			return user_id;
		}


		public void setUser_id(Integer user_id) {
			this.user_id = user_id;
		}


		public double getPrice() {
			return price;
		}


		public void setPrice(double price) {
			this.price = price;
		}


		public String getPay_status_text() {
			return pay_status_text;
		}


		public void setPay_status_text(String pay_status_text) {
			this.pay_status_text = pay_status_text;
		}

		public double getSource() {
			return source;
		}

		public void setSource(double source) {
			this.source = source;
		}
		
		@Override
		public String toString() {
			return "OrderListVOData [_id=" + _id + ", order_id=" + order_id
					+ ", item_name=" + item_name + ", item_picurl="
					+ item_picurl + ", create_time=" + create_time
					+ ", pay_type=" + pay_type + ", pay_status=" + pay_status
					+ ", pay_status_text=" + pay_status_text + ", user_id="
					+ user_id + ", price=" + price + ", source=" + source + "]";
		}


		@Override
		public int compareTo(OrderListVOData arg0) {
			if(this.create_time > arg0.getCreate_time()){
				return 1;
			}else if(this.create_time < arg0.getCreate_time()){
				return -1;
			}else{
				return 0;
			}
		}


		public String getCreate_time_text() {
			
			
			return create_time_text;
		}
		
		public String GetStringDate() {
		   try {
			return	DataUtils.dateToStringNormal(this.create_time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return "";
		}
		}


		public void setCreate_time_text(String create_time_text) {
			this.create_time_text = create_time_text;
		}


		public String getItem_id() {
			return item_id;
		}


		public void setItem_id(String item_id) {
			this.item_id = item_id;
		}

	}

}