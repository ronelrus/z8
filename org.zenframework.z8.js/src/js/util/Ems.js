Z8.define('Z8.util.Ems', {
	shortClassName: 'Ems',

	statics: {
		Base: parseFloat(DOM.getComputedStyle(document.body).fontSize),

		UnitHeight: 4,
		UnitSpacing: .71428571,

		unitsToEms: function(units) {
			return units ? units * Ems.UnitHeight + (units - 1) * Ems.UnitSpacing : 0;
		},

		pixelsToEms: function(pixels) {
			return pixels ? (pixels / Ems.Base).round(8) : 0;
		},

		emsToPixels: function(ems) {
			return Math.ceil(ems * Ems.Base);
		},

		enlarge: function(value) {
			var base = Ems.Base + value;
			if(8 <= base && base <= 17) {
				Ems.Base = base;
				DOM.setStyle(document.body, 'font-size', base + 'px');
				DOM.setStyle(document.body, 'line-height', base + 'px');
				DOM.callWindowResize();
			}
		}
	}
});