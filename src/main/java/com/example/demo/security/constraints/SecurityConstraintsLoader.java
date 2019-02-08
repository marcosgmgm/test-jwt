package com.example.demo.security.constraints;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SecurityConstraintsLoader {

    private ConstraintsMapper constraintsMapper;

    @PostConstruct
    private void initConstraintsMapper() throws Exception {
        ClassPathResource resource = new ClassPathResource("security-constraints.yml");
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        constraintsMapper = mapper.readValue(resource.getFile(), ConstraintsMapper.class);
    }

    public ConstraintsMapper getConstraintsMapper() {
        return constraintsMapper;
    }

}
