# Budgerigar

Budgerigar is a powerful, Spring Boot-based solution designed to index shared document directories and enable efficient search through Elasticsearch. This project supports over ten commonly used document formats and provides seamless integration with diverse file types, making it an indispensable tool for file and bookmark management.

## Key Features

1. **Comprehensive Document Indexing**
   - Supports indexing of a wide range of document formats, including Office files (Word, Excel, PowerPoint), PDF, HTML, Markdown, and more.
   - Automatically scans shared directories to build and update the search index in real time.

2. **Advanced Bookmark Parsing and Indexing**
   - Processes browser bookmark files, extracting and indexing each URL for quick retrieval.
   - Enables users to search through bookmarks alongside other document types effortlessly.

3. **Elasticsearch-Powered Search**
   - Leverages the capabilities of Elasticsearch to provide lightning-fast, full-text search across indexed documents.
   - Offers highly customizable search queries to suit diverse user requirements.

4. **User-Friendly Integration and Scalability**
   - Built on Spring Boot, ensuring ease of deployment and integration into existing systems.
   - Designed to handle extensive document collections and adapt to growing organizational needs.

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 11 or higher
- Elasticsearch server
- Maven or Gradle for dependency management

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/paganini2008/budgerigar.git
   ```
2. Navigate to the project directory:
   ```bash
   cd budgerigar
   ```
3. Build and run the application:
   ```bash
   mvn spring-boot:run
   ```

4. Configure Elasticsearch settings in the `application.properties` file.

### Usage
- Place your documents and bookmark files in the designated shared directory.
- Use the integrated search interface to locate files or URLs quickly.

## Contributing
We welcome contributions to enhance Budgerigar! Please fork the repository, create a feature branch, and submit a pull request.

## License
This project is licensed under the MIT License. See the `LICENSE` file for details.

---
For detailed documentation and examples, visit the [project repository](https://github.com/paganini2008/budgerigar).
