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
		<form:form id="refinement" method="post" modelAttribute="formBean"
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
		</form:form>
	</div>


	<script type="text/javascript">
		window.myData = [];
		window.statusError = false;
		window.stop = false;
		window.myData.box = null;
		window.map = null;
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
		
		function plotIncidents(inputData) {
			var outData = new Object();
			outData.allIncidentsData = [];
			var $msg = $("#msg");
			$msg.removeClass("error");
			$msg.addClass("warning");
			$msg.html("<p><b>Loading points...</b></p>");
			var urlAjax = "ajax/getIncidents.json";
			outData.statusOk = true;
			$
					.ajax({
						type : "post",
						url : urlAjax,
						data : inputData,
						dataType : 'json',
						beforeSend : function(xhr) {
							xhr.setRequestHeader("Accept", "application/json");
							xhr.setRequestHeader("Content-Type", "application/json");
						},
						complete : function(xhr, status) {
							var data = xhr.responseText;
							var parsed = $.parseJSON(data);
							var status = parsed.status;
							if (status == "OK") {
									var allIncidents = parsed.incidentArray;
									var allIncidentsData = [];
									for ( var i = 0; i < allIncidents.length; i++) {
										var temp = [];
										temp.push(allIncidents[i].latit);
										temp.push(allIncidents[i].longit);
										temp.push("");
										allIncidentsData.push(temp);
									}
									outData.allIncidentsData = allIncidentsData;
									reDrawMap(outData);
								}
							else {
								outData.statusOk = false;
								outData.errors = parsed.errors;
							}
						},
						error : function(xhr) {
							outData.statusOk = false;
						}
					});
		}

		function reDrawMap(dataPoints) {
			if (!window.myData.mapExists) {
				var $msg = $("#msg");
				var mapEl = $("#map")[0];
				var zoom = 10;
				if (typeof window.myData.mapCenter == 'undefined') {
					window.myData.mapCenter = new google.maps.LatLng(32.95733667, -117.1437767);
				}
				var $msg = $("#msg");
				if (dataPoints.statusOk) {
					window.map = drawSourceMap(mapEl, window.myData.mapCenter, zoom);
					drawIncidentsMap(dataPoints.allIncidentsData, map)
					areaChanged(window.map.getBounds());
					window.myData.mapExists = true;
					window.stop = true;
					$msg.removeClass("error info warning");
					$msg.addClass("info");
					$msg.html("Ok");
				} else {
					var errors = dataPoints.errors;
					var errorMessage = "";
					for (var i = 0; i < errors.length; i++) {
						 errorMessage = errorMessage + "<p>" + errors[i].message + "</p>";
					}
					$msg.html(errorMessage);
				}
			}
		}
		
		

		$(document).ready(function() {
							$("#msg").html("");
							
							
							$("#clearMap").click(function() {
								window.myData.mapExists = false;
								plotIncidents(JSON
										.stringify($('#refinement').serializeObject()));
							});

							$("#refinement").submit(function(event) {
												$(":submit").attr("disabled", true);
												var $msg = $("#msg");
												$msg.removeClass("error");
												$msg.addClass("warning");
												$msg.html("<p><b>Calculating...</b></p>");
												window.statusError = false;
												window.stop = false;
											
												var urlAjax = "ajax/processIncidents.json";
												$.ajax({
													type : "post",
													url : urlAjax,
													data : JSON
															.stringify($('#refinement').serializeObject()),
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
													complete : function(xhr, status) {
														var data = xhr.responseText;
														var parsed = $
																.parseJSON(data);

														var status = parsed.status;
														if (status == "OK") {
														
															var r_q = parsed.r_q;
															
															var resultBounds = new google.maps.LatLngBounds(
																      new google.maps.LatLng(r_q[2], r_q[3]),
																      new google.maps.LatLng(r_q[0], r_q[1])
																  );
															
															var dataBorders = new google.maps.Rectangle({
																bounds : resultBounds,
																strokeColor : '#ED9121',
																strokeOpacity : 0.8,
																strokeWeight : 2,
																fillColor : '#ED9121',
																fillOpacity : 0.25,
																clickable : false,
																editable : false,
																draggable : false
															});
															
								
															dataBorders.setMap(window.map);
																
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
							
							plotIncidents(JSON
									.stringify($('#refinement').serializeObject()));
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