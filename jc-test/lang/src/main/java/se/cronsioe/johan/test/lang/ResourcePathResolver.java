package se.cronsioe.johan.test.lang;

public class ResourcePathResolver {

    public String resolve(Resource resource) {
        StringBuilder path = new StringBuilder();

        if (resource.getDirectory() != null)
        {
            path.append(resource.getDirectory());

            if (!path.toString().endsWith("/"))
            {
                path.append("/");
            }
        }

        path.append(resource.getName());

        return path.toString();
    }
}
