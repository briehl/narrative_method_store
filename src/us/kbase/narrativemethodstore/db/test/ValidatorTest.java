package us.kbase.narrativemethodstore.db.test;

import java.io.File;
import java.util.Collections;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import us.kbase.narrativemethodstore.ValidateMethodParams;
import us.kbase.narrativemethodstore.ValidationResults;
import us.kbase.narrativemethodstore.db.Validator;
import us.kbase.narrativemethodstore.util.TextUtils;

public class ValidatorTest {
    
    @Test
    public void testPublicationError() throws Exception {
        String methodSpecId = "generate_heatmaps";
        String specJson = readTestMethodValidationFile(methodSpecId, "spec.json");
        String displayYaml = readTestMethodValidationFile(methodSpecId, "display.yaml");
        ValidationResults vr = Validator.validateMethod(
                new ValidateMethodParams().withId(methodSpecId)
                .withSpecJson(specJson).withDisplayYaml(displayYaml)
                .withExtraFiles(Collections.<String,String>emptyMap()));
        Assert.assertEquals("" + vr, 1L, (long)vr.getIsValid());
    }

    @Test
    public void testGroupParameters() throws Exception {
        ValidationResults vr = validate(1);
        Assert.assertEquals("" + vr, 1L, (long)vr.getIsValid());
    }

    @Test
    public void testGroupNotPresentInDisplay() throws Exception {
        ValidationResults vr = validate(8);
        Assert.assertEquals("" + vr, 0L, (long)vr.getIsValid());
        Assert.assertEquals("" + vr, 1, vr.getErrors().size());
        Assert.assertEquals("" + vr, "Can't find property [log_entries] within path " +
        		"[parameter-groups] in display.yaml", vr.getErrors().get(0));
    }
    
    private static String readTestMethodValidationFile(String methodSpecId, 
            String fileName) throws Exception {
        return FileUtils.readFileToString(new File(new File(
                "test/data/validation/methods", methodSpecId), fileName));
    }
    
    private static ValidationResults validate(int num) throws Exception {
        String specJson = loadTextResource("spec_" + num + ".properties");
        String displayYaml = loadTextResource("display_" + num + ".properties");
        return Validator.validateMethod(
                new ValidateMethodParams().withId("")
                .withSpecJson(specJson).withDisplayYaml(displayYaml)
                .withExtraFiles(Collections.<String,String>emptyMap()));
    }

    private static String loadTextResource(String name) throws Exception {
        return TextUtils.text(NarrativeMethodDataTest.class.getResourceAsStream(name));
    }
}
