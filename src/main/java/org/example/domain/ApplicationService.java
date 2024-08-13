package org.example.domain;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {

    public List<Resolution> getData(String resolution) throws Exception {
        try {
            ResolutionCache resolutionCache = new ResolutionCache();
            return resolutionCache.perFetch(resolution,1.0);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
