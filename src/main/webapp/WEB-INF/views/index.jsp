<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false"%>
<html>
<head>
<title>Query refinement</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="<c:url value="/resources/form.css" />" rel="stylesheet"
	type="text/css" />
	<script type="text/javascript"
		src="<c:url value="/resources/jquery/1.10.2/jquery.js" />"></script>
</head>
<body>
	<div id="formsContent">
		<form:form id="form" method="post" modelAttribute="formBean"
			cssClass="cleanform" action="${pageContext.request.contextPath}/">
			<p>Query refinement</p>
			<div>
			
				<fieldset>
					<legend>Map data:</legend>

					<form:label path="nELat">
		  			NE Latitude <form:errors path="nELat" cssClass="error" />
					</form:label>
					<form:input path="nELat" />

					<form:label path="nELng">
		  			NE Longitude <form:errors path="nELng" cssClass="error" />
					</form:label>
					<form:input path="nELng" />

					<form:label path="sWLat">
		  			SW Latitude <form:errors path="sWLat" cssClass="error" />
					</form:label>
					<form:input path="sWLat" />

					<form:label path="sWLng">
		  			SW Longitude <form:errors path="sWLng" cssClass="error" />
					</form:label>
					<form:input path="sWLng" />

					<div id="map" style="width: 600; height: 480px;"></div>
					<p>
						<input id="clearMap" type="button" value="Clear Map">
					</p>
				</fieldset>
			</div>
			<p>
				<input type="submit" value="Calculate">
			</p>
			<div id="msg"></div>
			<fieldset id="outMapFieldset">
				<legend>Output</legend>
			</fieldset>
		</form:form>
	</div>


	<script type="text/javascript">
		window.myData = [];
		window.statusError = false;
		window.stop = false;
		window.myData.box = null;
		window.myData.mapExists = false;
		window.isBusiness = false;
		
		function pollStatus(){
			   setTimeout(function(){
			      $.ajax({ url: "ajax/status.json", success: function(data){
			    	  if (!window.statusError){
			    		  if (!window.stop) {
			    	 	     $("#msg").html(data.message + " " + data.progress);
                    		  pollStatus();
                     	 	}
                     	 }
			      }, dataType: "json",
					beforeSend : function(
							xhr) {
						xhr
								.setRequestHeader(
										"Accept",
										"application/json");
						xhr
								.setRequestHeader(
										"Content-Type",
										"application/json");
					},});
			  }, 5000);
		     
		}

		function drawOutMap(data, diverData) {
			var drawDataBorders = true;
			var zoom = 6;
			var mapEl = $("#outMap")[0];
			var latitude = data[0][0];
			var longitude = data[0][1];
			var outMapCenter = new google.maps.LatLng(latitude, longitude);
			var outMap = new google.maps.Map(mapEl, {
				center : outMapCenter,
				zoom : zoom
			});
			drawMap(data, outMap);
			drawMap2(diverData, outMap);
			// if is map, draw borders
			if (drawDataBorders && window.myData.box != null) {
				var dataBorders = new google.maps.Rectangle({
					bounds : window.myData.box,
					strokeColor : '#FF33CC',
					strokeOpacity : 0.8,
					strokeWeight : 2,
					fillColor : '#FFE6F9',
					fillOpacity : 0.25,
					clickable : false,
					editable : false,
					draggable : false
				});

				dataBorders.setMap(outMap);
			}
		}

		function reDrawMap() {
			if (!window.myData.mapExists) {
				var mapEl = $("#map")[0];
				var zoom = 10;
				if (typeof window.myData.mapCenter == 'undefined') {
					window.myData.mapCenter = new google.maps.LatLng(32.95733667, -117.1437767);
				}
				map = drawSourceMap(mapEl, window.myData.mapCenter, zoom);
				areaChanged(map.getBounds());
				window.myData.mapExists = true;
			}
		}

		$(document).ready(function() {
							$("#msg").html("");
							reDrawMap();
							$("#clearMap").click(function() {
								window.myData.mapExists = false;
								reDrawMap();
							});

							$("#form").submit(function(event) {
												$(":submit").attr("disabled", true);
												var $msg = $("#msg");
												$msg.removeClass("error");
												$msg.addClass("warning");
												$msg.html("<p><b>Calculating...</b></p>");
												window.statusError = false;
												window.stop = false;
												// Remove the map
												$("#outMap").remove();
											
												var urlAjax = "ajax/processIncidents.json";
												
												if (window.isBusiness) {
													urlAjax = "ajax/process.json";
												}
												
												$.ajax({
													type : "post",
													url : urlAjax,
													data : JSON
															.stringify($('#form').serializeObject()),
													dataType : 'json',
													beforeSend : function(
															xhr) {
														xhr
																.setRequestHeader(
																		"Accept",
																		"application/json");
														xhr
																.setRequestHeader(
																		"Content-Type",
																		"application/json");
													},
													complete : function(
															xhr, status) {
														var data = xhr.responseText;
														var parsed = $
																.parseJSON(data);

														var status = parsed.status;
														if (status == "OK") {
															if (window.isBusiness) {
															var allBussines = parsed.bussinesArray;
															var allBussinesData = [];
															for (var i = 0; i < allBussines.length; i++) {
																var temp = [];
																temp
																		.push(allBussines[i].latit);
																temp
																		.push(allBussines[i].longit);
																temp
																		.push(allBussines[i].category);
																allBussinesData
																		.push(temp);
															}
															
															var allBussinesRef = parsed.bussinesRefArray;
															var allBussinesRefData = [];
															for (var i = 0; i < allBussinesRef.length; i++) {
																var temp = [];
																temp
																		.push(allBussinesRef[i].latit);
																temp
																		.push(allBussinesRef[i].longit);
																temp
																		.push(allBussinesRef[i].category);
																allBussinesRefData
																		.push(temp);
															}
															
															if (typeof overlay != 'undefined') {
																overlay
																		.setMap(null);
															}
															$("#outMapFieldset").append('<div id="outMap" style="width: 600px; height: 480px;"></div>');
															overlay = drawOutMap(allBussinesData, allBussinesRefData);
															} else {
																var allIncidents = parsed.incidentArray;
																var allIncidentsData = [];
																for (var i = 0; i < allIncidents.length; i++) {
																	var temp = [];
																	temp
																			.push(allIncidents[i].latit);
																	temp
																			.push(allIncidents[i].longit);
																	temp
																			.push(allIncidents[i].category);
																	allIncidentsData
																			.push(temp);
																}
																
																var allIncidentsRef = parsed.incidentRefArray;
																var allIncidentsRefData = [];
																for (var i = 0; i < allIncidentsRef.length; i++) {
																	var temp = [];
																	temp
																			.push(allIncidentsRef[i].latit);
																	temp
																			.push(allIncidentsRef[i].longit);
																	temp
																			.push(allIncidentsRef[i].category);
																	allIncidentsRefData
																			.push(temp);
																}
																
																if (typeof overlay != 'undefined') {
																	overlay
																			.setMap(null);
																}
																$("#outMapFieldset").append('<div id="outMap" style="width: 600px; height: 480px;"></div>');
																overlay = drawOutMap(allIncidentsData, allIncidentsRefData);
															}
                                                            window.stop = true;
															$msg.removeClass("error info warning");
															$msg.addClass("info");
															$msg.html("Ok");
														}
														if (status == "ERROR") {
															window.statusError = true;
															window.stop = true;
															$msg.removeClass("info warning");
															$msg.addClass("error");
															var errors = parsed.errors;
															var errorMessage = "";
															for (var i = 0; i < errors.length; i++) {
																 errorMessage = errorMessage + "<p>" + errors[i].message + "</p>";
															}
															$msg.html(errorMessage);
														}
														$(":submit").removeAttr("disabled");
													},
													error : function(
															xhr) {
														window.statusError = true;
														window.stop = true;
														$msg.removeClass("info warning");
														$msg.addClass("error");
														$msg.html(
																		"<p>Error executing AJAX call</p>");
														$(":submit")
																.removeAttr(
																		"disabled");
													}
												});

												event.preventDefault();
											});
						});
	</script>
	<script type="text/javascript"
		src="https://maps.googleapis.com/maps/api/js?libraries=places,drawing&amp;sensor=true&amp;key=AIzaSyCXlPs7edJYVCqhzfll3mozU5vuFc7T1Xk">
	</script>
	<script type="text/javascript"
		src="<c:url value="/resources/d3/d3.min.js" />"></script>
	<script type="text/javascript"
		src="<c:url value="/resources/form.js" />"></script>
	<script type="text/javascript" src="<c:url value="/resources/d3.js" />"></script>
	<script type="text/javascript"
		src="<c:url value="/resources/diverVis.js" />"></script>
</body>
</html>