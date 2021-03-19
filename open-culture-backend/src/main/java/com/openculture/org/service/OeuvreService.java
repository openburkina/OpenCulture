package com.openculture.org.service;

import com.openculture.org.config.Constants;
import com.openculture.org.domain.Oeuvre;
import com.openculture.org.domain.enumeration.TypeFichier;
import com.openculture.org.repository.OeuvreRepository;
import com.openculture.org.service.dto.ArtisteDTO;
import com.openculture.org.service.dto.ArtisteOeuvreDTO;
import com.openculture.org.service.dto.InformationCivilDTO;
import com.openculture.org.service.dto.OeuvreDTO;
import com.openculture.org.service.mapper.OeuvreMapper;
import com.openculture.org.web.rest.OeuvreResource;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

/**
 * Service Implementation for managing {@link Oeuvre}.
 */
@Service
@Transactional
public class OeuvreService {

    private final Logger log = LoggerFactory.getLogger(OeuvreService.class);


    private final OeuvreRepository oeuvreRepository;

    private final OeuvreMapper oeuvreMapper;

    private final InformationCivilService informationCivilService;

    private final ArtisteService artisteService;

    private final ArtisteOeuvreService artisteOeuvreService;

    public OeuvreService(OeuvreRepository oeuvreRepository, OeuvreMapper oeuvreMapper, InformationCivilService informationCivilService, ArtisteService artisteService, ArtisteOeuvreService artisteOeuvreService) {
        this.oeuvreRepository = oeuvreRepository;
        this.oeuvreMapper = oeuvreMapper;
        this.informationCivilService = informationCivilService;
        this.artisteService = artisteService;
        this.artisteOeuvreService = artisteOeuvreService;
    }

    /**
     * Save a oeuvre.
     *
     * @param oeuvreDTO the entity to save.
     * @return the persisted entity.
     */
    public OeuvreDTO save(OeuvreDTO oeuvreDTO) throws Exception {
        log.debug("Request to save Oeuvre : {}", oeuvreDTO);
        if (validedOeuvre(oeuvreDTO)) {
            ArtisteOeuvreDTO artisteOeuvre = new ArtisteOeuvreDTO();

            if (oeuvreDTO.getArtisteId() != null) {
                artisteOeuvre.setArtisteId(oeuvreDTO.getArtisteId());
            } else {
                artisteOeuvre.setArtisteId(artisteService.save(oeuvreDTO.getArtisteDTO()).getId());
            }

            File media = new File(oeuvreDTO.getPathFile());
            oeuvreDTO.setFile_content(FileUtils.readFileToByteArray(media));
            String s[] = media.getName().split("\\.");
            oeuvreDTO.setFile_name(s[0]);
            oeuvreDTO.setFile_extension(s[1]);

            log.debug("\ntaille avant: {}",oeuvreDTO.getFile_content().length);

            oeuvreDTO.setFile_content(compressData(oeuvreDTO.getFile_content()));

            log.debug("\ntaille apres: {}",oeuvreDTO.getFile_content().length);

            Oeuvre oeuvre = oeuvreMapper.toEntity(oeuvreDTO);
            oeuvre = oeuvreRepository.save(oeuvre);

            artisteOeuvre.setOeuvreId(oeuvre.getId());
            artisteOeuvreService.save(artisteOeuvre);
            return oeuvreMapper.toDto(oeuvre);
        }
        else {
            throw new Exception("Certains paramètres concernant l'oeuvre sont manquants");
        }
    }

    public boolean validedOeuvre(OeuvreDTO oeuvreDTO){
        if (oeuvreDTO.getTitre() != null
/*            && oeuvreDTO.getTypeOeuvreId() != null
            && oeuvreDTO.getRegroupementId() != null*/
            && oeuvreDTO.getDateSortie()!=null
            && oeuvreDTO.getPathFile() != null)
            return true;
        return false;
    }

    public static byte[] compressData(byte[] contentFile){
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DeflaterOutputStream dStream = new DeflaterOutputStream(out);
            dStream.write(contentFile);
            dStream.flush();
            dStream.close();

            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] deCompressData(byte[] contentFile){
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InflaterOutputStream iStream = new InflaterOutputStream(out);
            iStream.write(contentFile);
            iStream.flush();
            iStream.close();

            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get all the oeuvres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OeuvreDTO> findAll(TypeFichier typeFichier,Pageable pageable) {
        log.debug("Request to get all Oeuvres");
        return oeuvreRepository.findAllByType(pageable,typeFichier)
            .map(oeuvreMapper::toDto);
    }


    /**
     * Get one oeuvre by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public OeuvreDTO findOne(Long id) {
        log.debug("Request to get Oeuvre : {}", id);
        Oeuvre oeuvre = oeuvreRepository.findById(id).get();
        int taille = oeuvre.getFile_content().length;
        oeuvre.setFile_content(deCompressData(oeuvre.getFile_content()));
        
        log.debug("taille compressee: {}", taille);
        log.debug("taille reelle: {}", oeuvre.getFile_content().length);

        return oeuvreMapper.toDto(oeuvre);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> readMedia(Long id) {
        log.debug("Request to get Oeuvre : {}", id);
        OeuvreDTO oeuvre =  (findOne(id));        

        if(isVideo(oeuvre.getFile_extension()))
            return ResponseEntity.ok()
            .contentLength(oeuvre.getFile_content().length)
            .contentType(MediaType.parseMediaType(getContentType(oeuvre.getFile_extension())))
            .body(new InputStreamResource(new ByteArrayInputStream(oeuvre.getFile_content())));
        else {
            return ResponseEntity.ok()
            .contentLength(oeuvre.getFile_content().length)
            .contentType(MediaType.parseMediaType(getContentType(oeuvre.getFile_extension())))
            .body(new InputStreamResource(new ByteArrayInputStream(oeuvre.getFile_content())));
        }    
    }

    public String getContentType(String contentType){

        if(contentType.equals("mp4"))
            contentType = Constants.CONTENT_TYPE_MP4;
        else if(contentType.equals("flv"))
            contentType = Constants.CONTENT_TYPE_FLV;
        else if(contentType.equals("wmv"))
            contentType = Constants.CONTENT_TYPE_WMV;
        else if(contentType.equals("avi"))
            contentType = Constants.CONTENT_TYPE_AVI;
        else if(contentType.equals("mp3"))
            contentType = Constants.CONTENT_TYPE_MP3; 
        else if(contentType.equals("mp2"))
            contentType = Constants.CONTENT_TYPE_MP2;    

        return contentType;
    }

    public boolean isVideo(String contentType){
        if(contentType.equals("flv") || contentType.equals("wmv") || contentType.equals("avi")){
            return true;
        } else {
            return false;
        }
    }


    public void readAudio(){
        // byte[] b;
        // Files.write(Paths.get("audio"),b);
        // File file = FileUtils.writeByteArrayToFile(file, b);
        // InputStream inputStream = new ByteArrayInputStream(b);
        // AudioFormat audioFormat = new AudioFormat();
        // AudioInputStream audioInputStream = new AudioInputStream(inputStream, , length);
    }

    /**
     * Delete the oeuvre by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Oeuvre : {}", id);
        oeuvreRepository.deleteById(id);
    }

    // public ResponseEntity<StreamingResponseBody> readVideo (String videoName){
    //     try {
    //         StreamingResponseBody streamingResponseBody;
    //         final HttpHeaders httpHeaders = new HttpHeaders();

    //         final Path filePath = getVideoPath(videoName);
    //         final long fileSize = Files.size(filePath);

    //         byte[] buffer = new byte[1024];

    //         httpHeaders.add(Constants.HEADER_CONTENT_TYPE, getContentType());
    //         httpHeaders.add(C, Long.toString(fileSize));
    //         streamingResponseBody = os -> {
    //             try (RandomAccessFile file = new RandomAccessFile(filePath.toFile(), "r")) {
    //                 long pos = 0;
    //                 file.seek(pos);

    //                 while (pos < fileSize) {
    //                     file.read(buffer);
    //                     os.write(buffer);
    //                     pos += buffer.length;
    //                 }

    //                 os.flush();
    //             } catch (Exception ignored) {
    //                 // Noop
    //             }
    //         };
    //         return new ResponseEntity<>(streamingResponseBody, httpHeaders, HttpStatus.OK);

    //     } catch (FileNotFoundException e) {
    //         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //     } catch (IOException e) {
    //         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }

    public Path getVideoPath(String videoName) throws FileNotFoundException {
        final URL videoResource = OeuvreResource.class.getClassLoader().getResource(videoName);
        if (videoResource != null){
            try {
                return Paths.get(videoResource.toURI());
            } catch (URISyntaxException e) {
                throw new FileNotFoundException();
            }
        }
        throw new FileNotFoundException();
    }
}
