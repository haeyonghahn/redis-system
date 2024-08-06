package org.example.domain;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {

    public List<Resolution> getData(String resolution) {
        ResolutionCache resolutionCache = new ResolutionCache();
        return resolutionCache.perFetch(resolution,1.0);
    }
}
