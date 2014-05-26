
function drawMap(data, map) {

	var overlay = new google.maps.OverlayView();

	// Add the container when the overlay is added to the map.
	overlay.onAdd = function() {
		var layer = d3.select(this.getPanes().overlayLayer).append("div").attr(
				"class", "stations");

		// Draw each marker as a separate SVG element.
		// We could use a single SVG, but what size would it have?
		overlay.draw = function() {
			var projection = this.getProjection(), padding = 10;

			var marker = layer.selectAll("svg").data(d3.entries(data)).each(
					transform) // update existing markers
			.enter().append("svg:svg").each(transform).attr("class", "marker");

			// Add a circle.
			marker.append("svg:circle").attr("r", 2.5).attr("cx", padding)
					.attr("cy", padding);

			function transform(d) {
				d = new google.maps.LatLng(d.value[0], d.value[1]);
				d = projection.fromLatLngToDivPixel(d);
				return d3.select(this).style("left", (d.x - padding) + "px")
						.style("top", (d.y - padding) + "px");
			}
		};
	};

	overlay.setMap(map);
	return map;
};

function drawIncidentsMap(data, map) {

	var overlay = new google.maps.OverlayView();

	// Add the container when the overlay is added to the map.
	overlay.onAdd = function() {
		var layer = d3.select(this.getPanes().overlayLayer).append("div").attr(
				"class", "stations");

		// Draw each marker as a separate SVG element.
		// We could use a single SVG, but what size would it have?
		overlay.draw = function() {
			var projection = this.getProjection(), padding = 10;

			var marker = layer.selectAll("svg").data(d3.entries(data)).each(
					transform) // update existing markers
			.enter().append("svg:svg").each(transform).attr("class", "marker");

			// Add a circle.
			marker.append("svg:circle").attr("r", 2.5).attr("cx", padding)
					.attr("cy", padding);

			function transform(d) {
				d = new google.maps.LatLng(d.value[0], d.value[1]);
				d = projection.fromLatLngToDivPixel(d);
				return d3.select(this).style("left", (d.x - padding) + "px")
						.style("top", (d.y - padding) + "px");
			}
		};
	};

	overlay.setMap(map);
	return map;
};

function drawMap2(data2, map) {

	var overlay2 = new google.maps.OverlayView();

	// Add the container when the overlay is added to the map.
	overlay2.onAdd = function() {

		var layer2 = d3.select(this.getPanes().overlayLayer).append("div")
				.attr("class", "stations");

		// Draw each marker as a separate SVG element.
		// We could use a single SVG, but what size would it have?
		overlay2.draw = function() {
			var projection2 = this.getProjection(), padding = 10;

			var marker = layer2.selectAll("svg").data(d3.entries(data2)).each(
					transform) // update
			// existing
			// markers
			.enter().append("svg:svg").each(transform).attr("class", "marker");

			// Add a circle.

			marker.append("svg:circle").attr("r", 2.5).attr("cx", padding)
					.attr("cy", padding).style("fill", "red");

			// Add a label.
			marker.append("svg:text").attr("x", padding + 7).attr("y", padding)
					.attr("dy", "1em").text(function(d) {
						return d.value[2];
					});

			function transform(d) {
				d = new google.maps.LatLng(d.value[0], d.value[1]);
				d = projection2.fromLatLngToDivPixel(d);
				return d3.select(this).style("left", (d.x - padding) + "px")
						.style("top", (d.y - padding) + "px");
			}
		};
	};
	overlay2.setMap(map);
};