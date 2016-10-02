package se.cronsioe.johan.test.lang;

public abstract class Resource {

    public static ResourceBuilder named(String name) {
        return new ResourceBuilder().withName(name);
    }

    public abstract String getName();

    public abstract String getContent();

    public abstract String getDirectory();


    public static class ResourceBuilder {

        private String name;
        private String content;
        private String directory;

        public ResourceBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public ResourceBuilder withContent(String content) {
            this.content = content;
            return this;
        }

        public ResourceBuilder inDirectory(String directory) {
            this.directory = directory;
            return this;
        }

        public Resource build() {

            return new Resource() {
                @Override
                public String getName() {
                    return name;
                }

                @Override
                public String getContent() {
                    return content;
                }

                @Override
                public String getDirectory() {
                    return directory;
                }
            };
        }
    }
}
