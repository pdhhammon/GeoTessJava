package gov.sandia.geotess.gui.extractdata.dialogs;

import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.gui.enums.FileOperation;
import gov.sandia.geotess.gui.enums.MethodHelp;
import gov.sandia.geotess.gui.enums.ParameterHelp;
import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;
import gov.sandia.geotess.gui.tools.AbstractModelNeededDialog;
import gov.sandia.geotess.gui.tools.AttributeCheckboxPanel;
import gov.sandia.geotess.gui.tools.DeepestShallowestComponents;
import gov.sandia.geotess.gui.tools.DeltaOrNComponents;
import gov.sandia.geotess.gui.tools.ErrorLabel;
import gov.sandia.geotess.gui.tools.FileIOComponents;
import gov.sandia.geotess.gui.tools.HorizontalInterpolationComponents;
import gov.sandia.geotess.gui.tools.PopupLabel;
import gov.sandia.geotess.gui.tools.RadialInterpolationComponents;
import gov.sandia.geotess.gui.tools.RadioButtonPanel;
import gov.sandia.geotess.gui.tools.ReciprocalComponents;
import gov.sandia.geotess.gui.tools.TitleFieldComponents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class Values3DBlockDialog extends AbstractModelNeededDialog {

    private TitleFieldComponents lat1;
    private TitleFieldComponents lat2;
    private TitleFieldComponents lon1;
    private TitleFieldComponents lon2;
    private DeltaOrNComponents latSpacing;
    private DeltaOrNComponents lonSpacing;
    private DeepestShallowestComponents layers;
    private RadioButtonPanel radialDimension;
    private ReciprocalComponents reciprocal;
    private TitleFieldComponents maxRadialSpacing;
    private HorizontalInterpolationComponents horizontal;
    private RadialInterpolationComponents radial;
    private AttributeCheckboxPanel attributes;
    private FileIOComponents output;

    public Values3DBlockDialog(GeoTessPresenter presenter, JFrame parent) {
        super(presenter, parent, "Values 3D Block");
    }

    @Override
    public JPanel makeNewDialog(GeoTessModel model) {
        JPanel p = new JPanel();
        p.setLayout(new MigLayout("debug", "grow" ));

        this.latSpacing = new DeltaOrNComponents("Latitude Spacing: ");
        this.lonSpacing = new DeltaOrNComponents("Longitude Spacing: ");
        this.layers = new DeepestShallowestComponents(model);
        this.radialDimension = new RadioButtonPanel("Depth", "Radius", "Layer Index");
        this.maxRadialSpacing = new TitleFieldComponents("Max Radial Spacing: ", 5, ParameterHelp.MAX_RADIAL_SPACING);
        this.horizontal = new HorizontalInterpolationComponents();
        this.radial = new RadialInterpolationComponents();
        this.reciprocal = new ReciprocalComponents();
        this.attributes = new AttributeCheckboxPanel(model);
        this.output = new FileIOComponents("Output File: ", FileOperation.SAVE, parent, ParameterHelp.OUTPUT);
        this.lon1 = new TitleFieldComponents("First Longitude: ", 7, "degrees", ParameterHelp.FIRST_LON);
        this.lon2 = new TitleFieldComponents("Last Longitude: ", 7, "degrees", ParameterHelp.LAST_LON);
        this.lat1 = new TitleFieldComponents("First Latitude: ", 7, "degrees", ParameterHelp.FIRST_LAT);
        this.lat2 = new TitleFieldComponents("Last Latitude: ", 7, "degrees", ParameterHelp.LAST_LAT);
        
        p.add(lat1.getTitle());
        p.add(lat1.getTextBox());
        p.add(lat2.getTitle());
        p.add(lat2.getTextBox(), "wrap");
        
        p.add(lon1.getTitle());
        p.add(lon1.getTextBox());
        p.add(lon2.getTitle());
        p.add(lon2.getTextBox(), "wrap");
        
        p.add(latSpacing.getTitle());
        p.add(latSpacing.getButtons());
        p.add(latSpacing.getTextField(), "wrap");
        
        p.add(lonSpacing.getTitle());
        p.add(lonSpacing.getButtons());
        p.add(lonSpacing.getTextField(), "wrap");
        
        p.add(layers.getDeepLabel());
        p.add(layers.getDeepBox(), "wrap");

        p.add(layers.getShallowLabel());
        p.add(layers.getShallowBox(), "wrap");

        p.add(new PopupLabel("Radial Dimension: ", ParameterHelp.RADIAL_DIMENSION));
        p.add(radialDimension, "wrap");

        p.add(maxRadialSpacing.getTitle());
        p.add(maxRadialSpacing.getTextBox(), "wrap");

        p.add(horizontal.getTitle());
        p.add(horizontal.getButtons(), "wrap");

        p.add(radial.getTitle());
        p.add(radial.getButtons(), "wrap");

        p.add(reciprocal.getTitle());
        p.add(reciprocal.getButtons(), "wrap");
        
        p.add(attributes, "grow, push, spanx 2, wrap");
        p.add(output.getTitle());
        p.add(output.getTextBox());
        p.add(output.getButton(), "wrap");

        return p;
    }

    @Override
    public ActionListener getAcceptButtonListener() {

        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    presenter.values3dBlock(lat1.getFieldValue(), lat2.getFieldValue(), latSpacing.getTextField().getText(),
                            lon1.getFieldValue(), lon2.getFieldValue(), lonSpacing.getTextField().getText(),
                            layers.getDeepestIndex(), layers.getShallowestIndex()
                            , radialDimension.getSelected(), Double.parseDouble(maxRadialSpacing.getFieldValue()),
                            horizontal.getInterpolation(), radial.getInterpolation(),
                           reciprocal.getSelected(), attributes.getCheckedAttributeIndexes(),
                            output.getText());
                    destroy();
                } catch(Exception ee) {
                	setErrorVisible(true);
                }
            }
        };
    }

    @Override
    public String methodHelp() {
        return MethodHelp.VALUES_3D_BLOCK.getMethodTip();
    }
}
