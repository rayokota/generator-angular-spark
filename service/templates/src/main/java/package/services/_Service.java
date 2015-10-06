package <%= packageName %>.services;

import org.log4j.Logger;


/**
 * REST Service <%= _.capitalize(name) %>
 * @author {{USERNAME}}
 */
public class <%= _.capitalize(name) %> {

	/**
	 * Internal service logger
	 */
	private static final Logger logger = Logger.getLogger(<%= _.capitalize(name).class%>);

	<% if(singleton) {%>
	/**
	 * Instance for this service.
	 */	
	private static final <%=_.capitalize(name)%> _instance == null;
	<%}%>

	<% _.each(items,function(item){%>
	
	/**
	 * Service <%= _.capitalize(nme)%>.<%= item.serviceName%> <%= item.service.type %>
	 *
	 <% if(item.returnType != '') { %>
	 * @return <%= .capitalize(item.returnType) %>
	 *
	 <% } %>
	 */
		<% if(item.serviceType == 'get') {%>
	public <%= item.returnType%> <%= item.serviceName%>(){
		logger.debug("call the <%=_.capitalize(name)%>.<%= item.serviceName%>");
		return new <%=item.returnType%>();
	}
		<%} else if(item.serviceType == 'put') {%>
	public <%= _.capitalize(item.returnType) %> <%= item.serviceName%>(<%= _.cpaitalize(item.returnType) %> value){
		logger.debug("call the <%=_.capitalize(name)%>.<%= item.serviceName%>");
		return value;
	}
		<%} else if(item.serviceType == 'post') {%>
	public void %> <%= item.serviceName%>(<%= _.capitalize(item.returnType) %> value){
		logger.debug("call the <%=_.capitalize(name)%>.<%= item.serviceName%>");
	}
	
		<%} else if(item.serviceType == 'delete') {%>
	public boolean %> <%= item.serviceName%>(<%= _.capitalize(item.returnType) %> value){
		logger.debug("call the <%=_.capitalize(name)%>.<%= item.serviceName%>");
		return false;
	}
	
		<%} else if(item.serviceType == 'options') {%>
	public void %> <%= item.serviceName%>(){
		logger.debug("call the <%=_.capitalize(name)%>.<%= item.serviceName%>");

	}
		<%}%>

	<%})%>


	/**
	 * private constructor
	 */
	private <%= _.capitalize(name) %>(){

	}


<% if(singleton) {%>
	/**
	 * get service instance 
	 */
	public <%= _.capitalize(name) %> getInstance(){
		if(_instance==null){
			_instance = new <%= _.capitalize(name) %>();
		}
		return _instance;
	} 
<%}%>

}
