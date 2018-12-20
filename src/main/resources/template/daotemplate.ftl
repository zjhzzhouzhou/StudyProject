package ${packageName};

/**
 * Description:${modelName}dao层
 * User: ${author}
 * Date: ${date?string("yyyy-MM-dd HH:mm:ss")}
 */
@Repository
public class ${basicClass}Dao {

    @Autowired
    private ${basicClass}LogRepository logRepository;

    @Autowired
    private ${basicClass}Repository repository;

    /**
     * 根据条件查询${modelName}
     *
     * @param searchIModel
     * @return
     */
    public SearchResultModel<${basicOModel}> findModels(${basicClass}SearchIModel searchIModel) {
        Assert.notNull(searchIModel, "searchIModel must not be null");

        SearchResultModel<${basicOModel}> searchResultModel = null;
        List<${basicOModel}> oModels;

        Page<${basicClass}Entity> entitys;

        Specification<${basicClass}Entity> specification = SpecificationUtils.generateSpecification(searchIModel, ${basicClass}Entity.class);

        PageRequest pageRequest = new PageRequest(searchIModel.getOffset() - 1, searchIModel.getLimit());

        entitys = repository.findAll(specification, pageRequest);

        if (entitys != null) {
            searchResultModel = SearchResultModelUtils.createSearchResultModel(entitys);
            if (entitys.getTotalElements() > 0) {
                oModels = new ArrayList<>();
                for (${basicClass}Entity entity : entitys) {
                    ${basicOModel} oModel = parseEntity2Model(entity, ${basicOModel}.class);
                    if (oModel != null) {
                        oModels.add(oModel);
                    }
                }
                searchResultModel.setElements(oModels);
            }
        }

        return searchResultModel;
    }

    private <T> T parseEntity2Model(${basicClass}Entity entity, Class<T> clazz) {
        Assert.notNull(entity, "entity must not be null");
        Assert.isTrue(BeanUtils.notEmpty(entity, "id"), "entity must not be null");

        T model = BeanUtils.instantiate(clazz);
        BeanUtils.copyProperties(entity, model);

        return model;
    }

    /**
     * 查询指定${modelName}信息
     *
     * @param primaryKeyId
     * @return
     */

    public ${basicOModel} findModel(String primaryKeyId) {
        Assert.notNull(primaryKeyId, "primaryKeyId must not be null");

        ${basicOModel} oModel = null;

        ${basicClass}Entity entity = repository.findOne(primaryKeyId);
        if (entity != null) {
            oModel = new ${basicOModel}();
            BeanUtils.copyProperties(entity, oModel);
        }
        return oModel;
    }


    /**
     * 创建${modelName}
     *
     * @param iModel
     * @return
     */
    public ${basicOModel} createModel(${basicIModel} iModel) {
        Assert.notNull(iModel, "iModel must not be null");
        Assert.isTrue(BeanUtils.notEmpty(iModel, "primaryKeyId"), "iModel must not be null");
        return createOrUpdateModel(null, iModel);
    }

    /**
     * 更新${modelName}
     *
     * @param primaryKeyId
     * @param iModel
     * @return
     */
    public ${basicOModel} updateModel(String primaryKeyId, ${basicIModel} iModel) {
        Assert.isTrue(!StringUtils.isEmpty(primaryKeyId), "primaryKeyId must not be null");
        Assert.notNull(iModel, "iModel must not be null");
        Assert.isTrue(BeanUtils.notEmpty(iModel, "primaryKeyId"), "iModel must not be null");
        iModel.setUserId(primaryKeyId);
        return createOrUpdateModel(primaryKeyId, iModel);
    }

    /**
     * 创建或是新增${modelName}
     *
     * @param primaryKeyId
     * @param iModel
     * @return
     */
    public ${basicOModel} createOrUpdateModel(String primaryKeyId, ${basicIModel} iModel) {
        Assert.notNull(iModel, "iModel must not be null");
        ${basicOModel} oModel = null;
        //checkData(iModel);
        if (BeanUtils.notEmpty(iModel, "primaryKeyId")) {
            ${basicClass}LogEntity logEntity = new ${basicClass}LogEntity();

            //如果是修改，则将上次日志表中
            if (primaryKeyId != null) {
                ${basicClass}Entity oldEntity = repository.findOne(primaryKeyId);
                if (oldEntity != null && BeanUtils.notEmpty(oldEntity, "modelId")) {
                    ${basicIModel} IModel = parseEntity2Model(oldEntity, ${basicIModel}.class);
                    //如果该条记录有修改，需要记录日志
                    if (!iModel.equals(IModel)) {
                        BeanUtils.copyProperties(IModel, logEntity);
                    }
                }
            }

            ${basicClass}Entity entity = parseModel2Entity(primaryKeyId, iModel);
            repository.save(entity);
            if (logEntity != null && BeanUtils.notEmpty(logEntity, "modelReqLogId")) {
                logRepository.save(logEntity);
            }

            oModel = parseEntity2Model(entity, ${basicOModel}.class);
        }
        return oModel;
    }

    private ${basicClass}Entity parseModel2Entity(String primaryKeyId, ${basicIModel} iModel) {
        Assert.notNull(iModel, "IModel must not be null");
        Assert.isTrue(BeanUtils.notEmpty(iModel, "primaryKeyId"), "iModel must not be null");
        ${basicClass}Entity entity = null;

        if (primaryKeyId != null) {
            iModel.setUserId(primaryKeyId);
            entity = repository.findOne(primaryKeyId);
        }

        if (entity == null) {
            entity = new ${basicClass}Entity();
        }

        BeanUtils.copyProperties(iModel, entity);

        return entity;

    }
}
