package com.openculture.org.service;

import com.openculture.org.domain.Oeuvre;
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

/**
 * Service Implementation for managing {@link Oeuvre}.
 */
@Service
@Transactional
public class OeuvreService {

    private final Logger log = LoggerFactory.getLogger(OeuvreService.class);

    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_CONTENT_LENGTH = "Content-Length";
    private static final String HEADER_ACCEPT_RANGES = "Accept-Ranges";
    private static final String HEADER_CONTENT_RANGE = "Content-Range";
    private static final String CONTENT_TYPE_MP4 = "video/mp4";

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

            File video = new File(oeuvreDTO.getPathFile());
            oeuvreDTO.setFile_content(FileUtils.readFileToByteArray(video));
            oeuvreDTO.setFile_name(video.getName());
            String s[] = oeuvreDTO.getFile_name().split("\\.");
            oeuvreDTO.setFile_extension(s[1]);

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

    /**
     * Get all the oeuvres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OeuvreDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Oeuvres");
        return oeuvreRepository.findAll(pageable)
            .map(oeuvreMapper::toDto);
    }


    /**
     * Get one oeuvre by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OeuvreDTO> findOne(Long id) {
        log.debug("Request to get Oeuvre : {}", id);
        return oeuvreRepository.findById(id)
            .map(oeuvreMapper::toDto);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> readVideo(Long id) {
        log.debug("Request to get Oeuvre : {}", id);
        Oeuvre oeuvre =  oeuvreRepository.findById(id).get();
        return ResponseEntity.ok()
            .contentLength(oeuvre.getFile_content().length)
            .contentType(MediaType.parseMediaType(CONTENT_TYPE_MP4))
            .body(new InputStreamResource(new ByteArrayInputStream(oeuvre.getFile_content())));
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

    public ResponseEntity<StreamingResponseBody> readVideo (String videoName){
        try {
            StreamingResponseBody streamingResponseBody;
            final HttpHeaders httpHeaders = new HttpHeaders();

            final Path filePath = getVideoPath(videoName);
            final long fileSize = Files.size(filePath);

            byte[] buffer = new byte[1024];

            httpHeaders.add(HEADER_CONTENT_TYPE, CONTENT_TYPE_MP4);
            httpHeaders.add(HEADER_CONTENT_LENGTH, Long.toString(fileSize));
            streamingResponseBody = os -> {
                try (RandomAccessFile file = new RandomAccessFile(filePath.toFile(), "r")) {
                    long pos = 0;
                    file.seek(pos);

                    while (pos < fileSize) {
                        file.read(buffer);
                        os.write(buffer);
                        pos += buffer.length;
                    }

                    os.flush();
                } catch (Exception ignored) {
                    // Noop
                }
            };
            return new ResponseEntity<>(streamingResponseBody, httpHeaders, HttpStatus.OK);

        } catch (FileNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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
