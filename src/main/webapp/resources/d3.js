function areaChanged(coord) {
	if (typeof coord != 'undefined') {
		var nE = coord.getNorthEast();
		var sW = coord.getSouthWest();
		$('#nELat').val(nE.lat());
		$('#nELng').val(nE.lng());
		$('#sWLat').val(sW.lat());
		$('#sWLng').val(sW.lng());
		window.myData.box = coord;
	}
}

function drawSourceMap(el, mapCenter, mapZoom, followCenter) {

	var mapOptions = {
		center : mapCenter,
		zoom : mapZoom
	};

	var map = new google.maps.Map(el, mapOptions);

	var drawingManager = new google.maps.drawing.DrawingManager({
		drawingMode : google.maps.drawing.OverlayType.RECTANGLE,
		drawingControl : true,
		drawingControlOptions : {
			position : google.maps.ControlPosition.TOP_CENTER,
			drawingModes : [ google.maps.drawing.OverlayType.RECTANGLE ]
		},

		rectangleOptions : {
			fillColor : '#1E90FF',
			fillOpacity : 0.33,
			strokeWeight : 1,
			clickable : false,
			editable : true,
			zIndex : 1
		}
	});
	drawingManager.setMap(map);
	google.maps.event.addListener(drawingManager, 'rectanglecomplete',
			function(rectangle) {
				areaChanged(rectangle.getBounds());
			});

	google.maps.event.addListener(map, 'center_changed', function() {
		var newBounds = map.getBounds();
		window.myData.mapCenter = newBounds.getCenter();
	});

	return map;
}