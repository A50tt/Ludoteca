package com.ccsw.ludoteca.category;

import com.ccsw.ludoteca.category.model.Category;
import com.ccsw.ludoteca.category.model.CategoryDto;
import com.ccsw.ludoteca.common.exception.CommonException;
import com.ccsw.ludoteca.dto.StatusResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ccsw
 *
 */
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final String CREATION_SUCCESSFUL_EXT_MSG = "La categoría se ha creado.";
    private final String EDIT_SUCCESSFUL_EXT_MSG = "La categoría se ha modificado.";
    private final String DELETE_SUCCESSFUL_EXT_MSG = "La categoría se ha eliminado.";

    private final CategoryRepository categoryRepository;
    private final CategoryGameHelperService helper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryGameHelperService helper) {
        this.categoryRepository = categoryRepository;
        this.helper = helper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Category get(Long id) {

        return this.categoryRepository.findById(id).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Category> findAll() {

        return (List<Category>) this.categoryRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StatusResponse save(Long id, CategoryDto dto) {
        // Se ha introducido un Category sin 'name'
        if (dto.getName().isEmpty()) {
            return new StatusResponse(CommonException.MISSING_REQUIRED_FIELDS, CommonException.MISSING_REQUIRED_FIELDS_EXTENDED);
        }

        // Tenemos en cuenta si es edición o creación de 'Category' para devolver el mensaje correspondiente.
        Category category;
        boolean isUpdate = false;

        // Recuperamos una 'Category' con ese 'id' en la BBDD si existe y definimos la operación como UPDATE.
        // Si no existe, la inicializamos sin valores.
        if (id == null) {
            category = new Category(); // Inicializamos el objeto (sin valores).
        } else {
            category = this.get(id);
            if (category != null) { // Si se ha encontrado una 'Category' con ese 'id' en la BBDD, es UPDATE.
                isUpdate = true;
            }
        }
        BeanUtils.copyProperties(dto, category, "id");
        category.setName(dto.getName());

        try {
            this.categoryRepository.save(category); // Si da Exception o no y según UPDATE, devuelve un body u otro.
            if (isUpdate) {
                return new StatusResponse(StatusResponse.OK_REQUEST_MSG, EDIT_SUCCESSFUL_EXT_MSG);
            }
            return new StatusResponse(StatusResponse.OK_REQUEST_MSG, CREATION_SUCCESSFUL_EXT_MSG);
        } catch (Exception ex) {
            return new StatusResponse(CommonException.DEFAULT_ERROR, CommonException.DEFAULT_ERROR_EXTENDED);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StatusResponse delete(Long id) throws Exception {
        if (this.get(id) == null) {
            return new StatusResponse(CategoryException.CATEGORY_ID_NOT_FOUND, CategoryException.CATEGORY_ID_NOT_FOUND_EXTENDED);
        } else if (helper.findGamesByCategory(id)) {
            return new StatusResponse(CategoryException.CATEGORY_HAS_GAMES, CategoryException.CATEGORY_HAS_GAMES_EXTENDED);
        }
        try {
            categoryRepository.deleteById(id);
            return new StatusResponse(StatusResponse.OK_REQUEST_MSG, DELETE_SUCCESSFUL_EXT_MSG);
        } catch (Exception e) {
            return new StatusResponse(CommonException.DEFAULT_ERROR, CommonException.DEFAULT_ERROR_EXTENDED);
        }
    }
}