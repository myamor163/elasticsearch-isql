package io.github.iamazy.elasticsearch.dsl.sql.parser.query.method.expr;

import com.google.common.collect.Maps;
import io.github.iamazy.elasticsearch.dsl.sql.exception.ElasticSql2DslException;
import io.github.iamazy.elasticsearch.dsl.sql.parser.query.method.MethodInvocation;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Collections;
import java.util.Map;

import static io.github.iamazy.elasticsearch.dsl.cons.CoreConstants.COLON;
import static io.github.iamazy.elasticsearch.dsl.cons.CoreConstants.COMMA;

/**
 * @author iamazy
 */
public abstract class AbstractParameterizedMethodExpression implements ParameterizedMethodExpression {



    protected abstract String defineExtraParamString(MethodInvocation invocation);

    @Override
    public Map<String, String> generateParameterMap(MethodInvocation invocation) {
        String extraParamString = defineExtraParamString(invocation);

        if (StringUtils.isBlank(extraParamString)) {
            return Collections.emptyMap();
        }

        Map<String, String> extraParamMap = Maps.newHashMap();
        for (String paramPair : extraParamString.split(COMMA)) {
            String[] paramPairArr = paramPair.split(COLON);
            if (paramPairArr.length == 2) {
                extraParamMap.put(paramPairArr[0].trim(), paramPairArr[1].trim());
            }
            else {
                throw new ElasticSql2DslException("Failed to parse query method extra param string!");
            }
        }
        return extraParamMap;
    }

    public Map<String, Object> generateRawTypeParameterMap(MethodInvocation invocation) {
        Map<String, String> extraParamMap = generateParameterMap(invocation);
        if (MapUtils.isNotEmpty(extraParamMap)) {
            return Maps.transformEntries(extraParamMap, (key, value) -> NumberUtils.isDigits(value) ? NumberUtils.createNumber(value) : value);
        }

        return Collections.emptyMap();
    }

    protected Boolean isExtraParamsString(String extraParams) {
        if (StringUtils.isBlank(extraParams)) {
            return Boolean.FALSE;
        }
        for (String paramPair : extraParams.split(COMMA)) {
            String[] paramPairArr = paramPair.split(COLON);
            if (paramPairArr.length != 2) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }
}
