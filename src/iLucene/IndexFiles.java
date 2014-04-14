package iLucene;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.demo.FileDocument;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * ����ĳһĿ¼�µ������ı��ļ�
 * 
 * @author wujinsong
 */
public class IndexFiles {
    private IndexFiles() {
    }
    
    static final File INDEX_DIR = new File("c:\\index");
    
    public static void main(String[] args) {
        
        if (INDEX_DIR.exists()) {
            System.out.println("Cannot save index to '" + INDEX_DIR + " ' directory,please delete it first");
            System.exit(1);
        }
        
        final File docDir = new File("C:\\kankan");
        if (!docDir.exists() || !docDir.canRead()) {
            System.out.println("Document directory '" + docDir.getAbsolutePath() + "' does not exist or is not readable, please check the path");
            System.exit(1);
        }
        
        Date start = new Date();
        try {
            IndexWriter writer = new IndexWriter(FSDirectory.open(INDEX_DIR), new StandardAnalyzer(Version.LUCENE_CURRENT), true,
                    IndexWriter.MaxFieldLength.LIMITED);
            System.out.println("Indexing to directory '" + INDEX_DIR + "'...");
            indexDocs(writer, docDir);
            System.out.println("Optimizing...");
            writer.optimize();
            writer.close();
            
            Date end = new Date();
            System.out.println(end.getTime() - start.getTime() + " total milliseconds!");
        }
        catch (IOException e) {
            System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
        }
    }
    
    /**
     * �����ļ�
     * 
     * @param writer
     * @param file
     * @throws CorruptIndexException
     * @throws IOException
     */
    static void indexDocs(IndexWriter writer, File file) throws CorruptIndexException, IOException {
        // ��Ҫ��ͼ�������ɶ����ļ�
        if (file.canRead()) {
            if (file.isDirectory()) {
                String[] files = file.list();
                // һ��IO������ܷ���
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        indexDocs(writer, new File(file, files[i]));
                    }
                }
            }
            else {
                System.out.println("adding " + file);
                try {
                    writer.addDocument(FileDocument.Document(file));
                }
                // ������windowsϵͳ��һЩ��ʱ�ļ��ᵼ�¡����ʾܾ����쳣������ļ��Ƿ���Ա���ȡ���������á�
                catch (FileNotFoundException exception) {
                    
                }
            }
        }
    }
}
