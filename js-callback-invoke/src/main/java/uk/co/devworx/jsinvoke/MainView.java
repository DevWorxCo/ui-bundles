package uk.co.devworx.jsinvoke;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import uk.co.devworx.jsinvoke.data.UnclaimedEstate;
import uk.co.devworx.jsinvoke.data.UnclaimedEstateFactory;

/**
 * The main view contains a button and a click listener.
 */
@Route("")
@PWA(name = "Java Script Grid Main Invoke", shortName = "JS Invoke", enableInstallPrompt = false)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@Theme(value = Lumo.class, variant = Lumo.DARK)
public class MainView extends VerticalLayout 
{
	private static final long serialVersionUID = 1L;
	
	private final Grid<UnclaimedEstate> unclaimedGrid;
	
	private final ListDataProvider<UnclaimedEstate> listDataProvider;
	private final List<UnclaimedEstate> unclaimedEstates;
	
	private final ComboBox<String> placesOfDeath;
	private final ComboBox<String> placeOfBirth;
	private final Button clearFilters;
	
	private final PlaceOfBirthPredicate placeOfBirthPred = new PlaceOfBirthPredicate();
	private final PlaceOfDeathPredicate placeOfDeathPred = new PlaceOfDeathPredicate();
		
	private void apply_col_settings(Column<UnclaimedEstate> col)
	{
		col.setResizable(true);
		col.setAutoWidth(true);
		col.setSortable(true);
	}
	
	@ClientCallable
	private void setPlaceOfBirthFromJS(String placeOfBirthParam)
	{
		placeOfBirth.setValue(placeOfBirthParam);
	}
	
	@ClientCallable
	private void setPlaceOfDeathFromJS(String placeOfDeathParam)
	{
		placesOfDeath.setValue(placeOfDeathParam);
	}
	
    public MainView() 
    {
    	unclaimedEstates = UnclaimedEstateFactory.getUnclaimedEstates();
    	
    	listDataProvider = new ListDataProvider<UnclaimedEstate>(unclaimedEstates);
    	listDataProvider.addFilter(placeOfBirthPred);
    	listDataProvider.addFilter(placeOfDeathPred);
    	
    	placesOfDeath = new ComboBox<String>("Place of Death", UnclaimedEstateFactory.getDistinctPlacesOfDeath());
    	placesOfDeath.setClearButtonVisible(true);
    	placesOfDeath.setPlaceholder("(No Filter Selected)");
    	placesOfDeath.setWidth("350px");
    	
    	placeOfBirth = new ComboBox<String>("Place of Birth", UnclaimedEstateFactory.getDistinctPlacesOfBirth());
    	placeOfBirth.setClearButtonVisible(true);
    	placeOfBirth.setPlaceholder("(No Filter Selected)");
    	placeOfBirth.setWidth("350px");
    	
    	clearFilters = new Button("Clear Filters");
    	clearFilters.addClickListener( cl -> 
    	{
    		placesOfDeath.setValue(null);
    		placeOfBirth.setValue(null);
    	});
    	
    	placesOfDeath.addValueChangeListener(vl -> 
    	{
    		String newValue = vl.getValue();
    		placeOfDeathPred.setPlaceOfDeath(newValue);
    		listDataProvider.refreshAll();
    	});
    	
    	placeOfBirth.addValueChangeListener(vl -> 
    	{
    		String newValue = vl.getValue();
    		placeOfBirthPred.setPlaceOfBirth(newValue);
    		listDataProvider.refreshAll();
    	});
    	
    	HorizontalLayout topPanel = new HorizontalLayout();
    	topPanel.add(placesOfDeath);
    	topPanel.add(placeOfBirth);
    	topPanel.add(clearFilters);

    	Label lbl = new Label("Unclaimed Estates Held By The UK Treasury - 2020");
    	lbl.setClassName("");
    	
    	
    	add(lbl);
    	
    	add(topPanel);
    	
    	int height = 800;//getWindowHeight();
    	
    	unclaimedGrid = new Grid<>();
    	unclaimedGrid.setWidthFull();
    	
    	String heightOfGrid = ((int)(Math.round(height * 0.8))) + "px";
    	
    	System.out.println("Grid Height : " + heightOfGrid);
    	
    	unclaimedGrid.setHeight( heightOfGrid  );
    	unclaimedGrid.setDataProvider(listDataProvider);
    	
    	apply_col_settings(unclaimedGrid.addColumn(ue -> ue.getBvReference()).setHeader("BV Reference"));
    	apply_col_settings(unclaimedGrid.addColumn(ue -> ue.getPlaceOfDeath()).setHeader("Place of Death"));    	
    	apply_col_settings(unclaimedGrid.addColumn(ue -> 
    	{
    		Optional<LocalDate> pubDate = ue.getDateOfPublicationDate();
    		Optional<String> pubDateStr = ue.getDateOfPublicationString();
    		if(pubDate.isPresent() == true) return pubDate.get().toString();
    		return pubDateStr.get();
    	}).setHeader("Publication Date"));
    	apply_col_settings(unclaimedGrid.addColumn(ue -> ue.getDateOfDeath()).setHeader("Date of Death"));    	
    	apply_col_settings(unclaimedGrid.addColumn(ue -> ue.getForename()).setHeader("Forename"));    	
    	apply_col_settings(unclaimedGrid.addColumn(ue -> ue.getSurname()).setHeader("Surname"));    	
    	apply_col_settings(unclaimedGrid.addColumn(ue -> 
    	{
    		if(ue.getDateOfBirth().isEmpty() == true)
    		{
    			return "";
    		}
    		else
    		{
    			return ue.getDateOfBirth().get().toString();
    		}
    	}).setHeader("Date of Birth"));
    	
    	apply_col_settings(unclaimedGrid.addColumn(ue -> ue.getPlaceOfBirth().orElse("")).setHeader("Place of Birth"));
    	
    	add(unclaimedGrid);
    }
    
	private int getWindowHeight()
	{
		final AtomicInteger ing = new AtomicInteger();
		UI.getCurrent().getPage().retrieveExtendedClientDetails(rc -> 
    	{
    		ing.set(rc.getWindowInnerHeight());
    	});
		return ing.get();
	}
	
	private int getWindowWidth()
	{
		final AtomicInteger ing = new AtomicInteger();
		UI.getCurrent().getPage().retrieveExtendedClientDetails(rc -> 
    	{
    		ing.set(rc.getWindowInnerWidth());
    	});
		return ing.get();
	}
	
	

	class PlaceOfBirthPredicate implements SerializablePredicate<UnclaimedEstate>
	{
		private volatile String placeOfBirth;
		
		@Override
		public boolean test(UnclaimedEstate t)
		{
			if(placeOfBirth == null) 
			{
				return true;
			}
			return t.getPlaceOfBirth().orElse("").equals(placeOfBirth);
		}
		
		public void setPlaceOfBirth(String b)
		{
			this.placeOfBirth = b;
		}
	}
	
	class PlaceOfDeathPredicate implements SerializablePredicate<UnclaimedEstate>
	{
		private volatile String placeOfDeath;
		
		@Override
		public boolean test(UnclaimedEstate t)
		{
			if(placeOfDeath == null) 
			{
				return true;
			}
			return t.getPlaceOfDeath().equals(placeOfDeath);
		}
		
		public void setPlaceOfDeath(String b)
		{
			this.placeOfDeath = b;
		}
	}
	
    
    
}
