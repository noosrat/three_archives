package common.fedora;

public enum DatastreamID {
	DC("DUBLIN CORE"), IMG("IMAGE"), IMG_LOW_RES("IMAGE"), VID("VIDEO"), AUD("AUDIO"), PDF("PDF");

	private String description;

	private DatastreamID(String type) {
		this.description = type;
	}

	public String getDescription() {
		return this.description;
	}

	public static DatastreamID parseDescription(String type) {
		for (DatastreamID id : DatastreamID.values()) {
			if (type.equalsIgnoreCase(id.getDescription())) {
				return id;
			}
		}
		return null;
	}

	public static DatastreamID parseMediaType(String format) {
		if (format != null) {
			String type = format.toLowerCase();
			if (type.contains("image") || type.contains("img") || type.contains("jpeg") || type.contains("jpg")
					|| type.contains("png") || type.contains("gif")) {
				return DatastreamID.IMG;
			} else if (type.contains("vid") || type.contains("3gp") || type.contains("mp4") || type.contains("mpeg4")) {
				return DatastreamID.VID;

			} else if (type.contains("mp3") || type.contains("aud") || type.contains("flac")) {
				return DatastreamID.AUD;
			} else if (type.contains("pdf") || type.contains("txt")) {
				return DatastreamID.PDF;
			}
		}

		return null;
	}
}
