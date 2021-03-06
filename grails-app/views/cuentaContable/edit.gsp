<%@ page import="com.luxsoft.impapx.contabilidad.CuentaContable" %>
<!doctype html>
<html>
	
	<head>
		<meta name="layout" content="taskView">
		<g:set var="entityName" value="${message(code: 'cuentaContable.label', default: 'CuentaContable')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
		<r:require module="luxorForms"/>
	</head>
	
	<body>
	
	<content tag="header">
		
 	</content>
 	
 	<content tag="consultas">
 		<li>
 			<g:link class="list" action="list">
				<i class="icon-list"></i>
				Cuentas
			</g:link>
		</li>		
 	</content>
 	<content tag="operaciones">
 		
 	</content>
 	
 	<content tag="document">
		
		<div class="accordion-group">
 			<div class="accordion-heading">
 				<a class="accordion-toggle alert" data-toggle="collapse" data-parent="#saldoDeCuentaAccordion" href="#collapseOne">
 					Cuenta:${cuentaContableInstance.clave} ${cuentaContableInstance.descripcion} 
 				</a>
 			</div>
 			<div id="collapseOne" class="accordion-body collapse ">
 				<div class="accordion-inner ">
 					<g:render template="form" bean="${cuentaContableInstance}"/>
 				</div>
 			</div>
 		</div>
 		
 		<div>
 			<g:render template="subCuentasPanel" bean="${cuentaContableInstance}" var="cuenta"/>
 			<%-- <g:render template="subCuentasPanel" model="[cuenta:cuentaContableInstance,subCuentas:subCuentas:subCuentas]"/>--%>
 		</div>
 	</content>
		
	</body>

</html>


