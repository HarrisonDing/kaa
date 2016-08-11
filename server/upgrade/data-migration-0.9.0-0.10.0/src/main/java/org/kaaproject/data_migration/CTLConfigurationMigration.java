package org.kaaproject.data_migration;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.kaaproject.data_migration.model.Schema;
import org.kaaproject.data_migration.model.Ctl;
import org.kaaproject.data_migration.model.CtlMetaInfo;
import org.kaaproject.data_migration.utils.datadefinition.DataDefinition;
import org.kaaproject.kaa.server.common.core.algorithms.generation.ConfigurationGenerationException;
import org.kaaproject.kaa.server.common.core.algorithms.generation.DefaultRecordGenerationAlgorithm;
import org.kaaproject.kaa.server.common.core.algorithms.generation.DefaultRecordGenerationAlgorithmImpl;
import org.kaaproject.kaa.server.common.core.configuration.RawData;
import org.kaaproject.kaa.server.common.core.configuration.RawDataFactory;
import org.kaaproject.kaa.server.common.core.schema.RawSchema;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import static java.util.stream.Collectors.joining;
import static org.kaaproject.data_migration.utils.datadefinition.Constraint.constraint;
import static org.kaaproject.data_migration.utils.datadefinition.ReferenceOptions.CASCADE;

public class CTLConfigurationMigration extends AbstractCTLMigration {


    public CTLConfigurationMigration(Connection connection) {
        super(connection);
    }

    @Override
    public void beforeTransform() throws SQLException {
        super.beforeTransform();

        // change FK constraint between table that contains data and appropriate <feature>_schems table
        dd.dropUnnamedFK("configuration", "configuration_schems");
        dd.alterTable(getName())
                .add(constraint("FK_configuration_schems_id")
                        .foreignKey("configuration_schems_id")
                        .references("configuration_schems", "id")
                        .onDelete(CASCADE)
                        .onUpdate(CASCADE)
                )
                .execute();
    }

    @Override
    protected String getName() {
        return "configuration";
    }


}
