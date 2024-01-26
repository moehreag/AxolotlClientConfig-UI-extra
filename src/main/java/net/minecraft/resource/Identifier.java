package net.minecraft.resource;

import lombok.Getter;

@Getter
public class Identifier {

	private final String namespace, path;

	public Identifier(String path){
		this(null, path);
	}

	public Identifier(String namespace, String path){
		if (namespace == null) {
			if (path.contains(":")) {
				String[] parts = path.split(":", 2);
				namespace = parts[0];
				path = parts[1];
			} else {
				namespace = "minecraft";
			}
		}

		this.namespace = namespace;
		if (!path.startsWith("/")){
			path = "/"+path;
		}
		this.path = path;
	}

	@Override
	public String toString() {
		if (namespace.equals("minecraft")){
			return path;
		} else {
			return "/assets/"+namespace+path;
		}
	}
}
