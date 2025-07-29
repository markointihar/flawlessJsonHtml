import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main application class for JSON to HTML conversion.
 *
 * Usage: java Main [input.json] [output.html]
 * If no arguments provided, defaults to "input.json" and "output.html"
 */
public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static final String DEFAULT_INPUT_FILE = "input.json";
    private static final String DEFAULT_OUTPUT_FILE = "output.html";
    private static final int SUCCESS_EXIT_CODE = 0;
    private static final int ERROR_EXIT_CODE = 1;

    public static void main(String[] args) {
        try {
            JsonHtmlConverterApp app = new JsonHtmlConverterApp();
            app.run(args);
            System.exit(SUCCESS_EXIT_CODE);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Application failed", e);
            System.err.println("Error: " + e.getMessage());
            System.exit(ERROR_EXIT_CODE);
        }
    }

    public static class JsonHtmlConverterApp {

        private final JsonHtmlConverter converter;

        public JsonHtmlConverterApp() {
            this.converter = new JsonHtmlConverter();
        }

        /**
         * Runs the conversion process.
         *
         * @param args command line arguments [inputFile] [outputFile]
         * @throws JsonHtmlConversionException if conversion fails
         * @throws IOException if file operations fail
         */
        public void run(String[] args) throws JsonHtmlConversionException, IOException {
            FileConfig config = parseArguments(args);

            LOGGER.info("Starting JSON to HTML conversion");
            LOGGER.info("Input file: " + config.inputFile());
            LOGGER.info("Output file: " + config.outputFile());

            validateInputFile(config.inputFile());

            JSONObject jsonData = readJsonFile(config.inputFile());
            String htmlOutput = convertToHtml(jsonData);
            writeHtmlFile(config.outputFile(), htmlOutput);

            System.out.println("✓ HTML successfully generated: " + config.outputFile());
            LOGGER.info("Conversion completed successfully");
        }

        private FileConfig parseArguments(String[] args) {
            return switch (args.length) {
                case 0 -> new FileConfig(
                        Paths.get(DEFAULT_INPUT_FILE),
                        Paths.get(DEFAULT_OUTPUT_FILE)
                );
                case 1 -> new FileConfig(
                        Paths.get(args[0]),
                        Paths.get(DEFAULT_OUTPUT_FILE)
                );
                case 2 -> new FileConfig(
                        Paths.get(args[0]),
                        Paths.get(args[1])
                );
                default -> throw new IllegalArgumentException(
                        "Usage: java Main [input.json] [output.html]"
                );
            };
        }

        private void validateInputFile(Path inputFile) throws IOException {
            if (!Files.exists(inputFile)) {
                throw new IOException("Input file not found: " + inputFile);
            }

            if (!Files.isReadable(inputFile)) {
                throw new IOException("Input file is not readable: " + inputFile);
            }

            if (Files.size(inputFile) == 0) {
                throw new IOException("Input file is empty: " + inputFile);
            }

            String fileName = inputFile.getFileName().toString().toLowerCase();
            if (!fileName.endsWith(".json")) {
                LOGGER.warning("Input file does not have .json extension: " + inputFile);
            }
        }

        private JSONObject readJsonFile(Path inputFile) throws IOException, JsonHtmlConversionException {
            try {
                String content = Files.readString(inputFile);
                if (content.trim().isEmpty()) {
                    throw new JsonHtmlConversionException("JSON file is empty or contains only whitespace");
                }

                return new JSONObject(content);

            } catch (JSONException e) {
                throw new JsonHtmlConversionException(
                        "Invalid JSON format in file: " + inputFile + ". " + e.getMessage(), e
                );
            } catch (IOException e) {
                throw new IOException("Failed to read input file: " + inputFile, e);
            }
        }

        private String convertToHtml(JSONObject jsonData) throws JsonHtmlConversionException {
            try {
                return converter.convert(jsonData);
            } catch (Exception e) {
                throw new JsonHtmlConversionException("Failed to convert JSON to HTML", e);
            }
        }

        private void writeHtmlFile(Path outputFile, String htmlContent) throws IOException {
            try {
                // Create parent directories if they don't exist
                Path parentDir = outputFile.getParent();
                if (parentDir != null && !Files.exists(parentDir)) {
                    Files.createDirectories(parentDir);
                }

                Files.writeString(outputFile, htmlContent);

            } catch (IOException e) {
                throw new IOException("Failed to write output file: " + outputFile, e);
            }
        }
    }

    public record FileConfig(Path inputFile, Path outputFile) {
        public FileConfig {
            if (inputFile == null) {
                throw new IllegalArgumentException("Input file path cannot be null");
            }
            if (outputFile == null) {
                throw new IllegalArgumentException("Output file path cannot be null");
            }
        }
    }
}