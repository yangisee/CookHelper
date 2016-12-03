package group88.cookhelper;

import android.app.Activity;
import android.content.res.AssetManager;
import android.support.v4.app.INotificationSideChannel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.view.*;
import android.text.*;
import android.content.*;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.io.Reader;
import java.io.InputStreamReader;

/**
 * Group Project
 * CookHelper
 */


public class MainActivity extends AppCompatActivity {
    private ListView recipeList;

    private String[] spinnerClass ={"Any","Beef", "Chicken","Pork","Seafood", "Veggie","Mixed"};
    private String[] spinnerOrigin= {"Any","Italian", "Chinese", "Midle Eastern", "Indian", "American"};
    private String[] spinnerCategory= {"Any","Starter", "Main Dish", "Desert=-p=[", "Drink", "Sauce", "Salad"};

    public static List<Recipe> allRecipe=new LinkedList<>();
    public static List<Recipe> filterResult=new LinkedList<>();
    public  List<String> showList=new LinkedList<String>();
    private int numOfFilteredRecipe;

    private static String savedSearch="";
    private static int savedClass=0;
    private static int savedOrigin =0;
    private static int savedCategory =0;


    EditText mEditText;
    Button mClearText;
    Button filter;
    Button reset;
    Spinner spClass;
    Spinner spOrigin;
    Spinner spCategory;
    int backButtonCount=0;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }


        filter = (Button) findViewById(R.id.filter);
        reset = (Button) findViewById(R.id.reset);

        mEditText = (EditText) findViewById(R.id.search);
        mEditText.setText(savedSearch);
        mClearText = (Button) findViewById(R.id.clearText);
        if(!mEditText.getText().toString().isEmpty()){
            mClearText.setVisibility(View.VISIBLE);
        }
        else {
            mClearText.setVisibility(View.GONE);
        }


        //This is how to add items to spinner:
        spClass = (Spinner) findViewById(R.id.SPclass);
        ArrayAdapter<String> adapterClass = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerClass);
        spClass.setAdapter(adapterClass);
        spClass.setSelection(savedClass);

        spOrigin = (Spinner) findViewById(R.id.SPorigin);
        ArrayAdapter<String> adapterOrigin = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerOrigin);
        spOrigin.setAdapter(adapterOrigin);
        spOrigin.setSelection(savedOrigin);

        spCategory = (Spinner) findViewById(R.id.SPcategory);
        ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerCategory);
        spCategory.setAdapter(adapterCategory);
        spCategory.setSelection(savedCategory);

        recipeList = (ListView) findViewById(R.id.recipe_list_view);
        ArrayAdapter adapterRecipe = new ArrayAdapter(this, android.R.layout.simple_list_item_1, showList);
        recipeList.setAdapter(adapterRecipe);


        //We use temperary array to show the list,
        //will be substitute by JSON reader


        // showList = new LinkedList<>();
        if(allRecipe.isEmpty()) {
//
//            {"Any","Beef", "Chicken","Pork","Seafood", "Vegie","Mixed"};
//             {"Any","Italian", "Chinese", "Midle Eastern", "Indian", "American"};
//           {"Any","Starter", "Main Dish", "Desert", "Drink", "Sauce", "Salad"};
            showList.clear();
            filterResult.clear();
            Recipe Steak = new Recipe();
            Steak.setRecipeName("Steak");
            Steak.setRecipeOrigin("American");
            Steak.setRecipeClass("Beef");
            Steak.setRecipeCategory("Main Dish");
            Steak.addIngredients(new Ingredient("balsamic vinegar",(float)0.5, Ingredient.Measure.cup));
            Steak.addIngredients(new Ingredient("soy sauce",(float)0.25, Ingredient.Measure.cup));
            Steak.addIngredients(new Ingredient("minced garlic",3, Ingredient.Measure.table_spoon));
            Steak.addIngredients(new Ingredient("honey",2, Ingredient.Measure.table_spoon));
            Steak.addIngredients(new Ingredient("olive oil",2, Ingredient.Measure.table_spoon));
            Steak.addIngredients(new Ingredient("ground black pepper",2, Ingredient.Measure.tea_spoon));
            Steak.addIngredients(new Ingredient("Worcestershire sauce",1, Ingredient.Measure.tea_spoon));
            Steak.addIngredients(new Ingredient("onion powder",2, Ingredient.Measure.tea_spoon));
            Steak.addIngredients(new Ingredient("salt",(float)0.5, Ingredient.Measure.tea_spoon));
            Steak.addIngredients(new Ingredient("liquid smoke flavoring",(float)0.5, Ingredient.Measure.tea_spoon));
            Steak.addIngredients(new Ingredient("rib-eye steaks",(float)2.5, Ingredient.Measure.pound));
            Steak.addSteps("Mix vinegar, soy sauce, garlic, honey, olive oil, ground black pepper, etc (all" +
                    "the ingredients) in a bowl");
            Steak.addSteps("Place steak in glass dish with the marinade and rub liquid onto the meat.");
            Steak.addSteps("Refrigerate for 1 -2 days");
            Steak.addSteps("Preheat grill to medium- high");
            Steak.addSteps("Lightly oil grill.");
            Steak.addSteps("Grill steaks 7 mins per side or desired doneness.");
            Steak.addSteps("Serve and enjoy!");
            allRecipe.add(Steak);

            Recipe VegiePho = new Recipe();
            VegiePho.setRecipeName("Veggie Pho");
            VegiePho.setRecipeOrigin("Any");
            VegiePho.setRecipeClass("Veggie");
            VegiePho.setRecipeCategory("Main Dish");
            VegiePho.addIngredients(new Ingredient("onion",1, Ingredient.Measure.piece));
            VegiePho.addIngredients(new Ingredient("shallot",(float)0.5,Ingredient.Measure.piece));
            VegiePho.addIngredients(new Ingredient("garlic cloves",(float)0.5,Ingredient.Measure.piece));
            VegiePho.addIngredients(new Ingredient("sliced ginger",1,Ingredient.Measure.piece));
            VegiePho.addIngredients(new Ingredient("cinnamon sticks",1, Ingredient.Measure.piece));
            VegiePho.addIngredients(new Ingredient("vegetable stock",2,Ingredient.Measure.cup));
            VegiePho.addIngredients(new Ingredient("soy sauce",2,Ingredient.Measure.table_spoon));
            VegiePho.addIngredients(new Ingredient("salt",1,Ingredient.Measure.tea_spoon));
            VegiePho.addIngredients(new Ingredient("riced noodles",1,Ingredient.Measure.pound));
            VegiePho.addIngredients(new Ingredient("tofu",8,Ingredient.Measure.ounce));
            VegiePho.addIngredients(new Ingredient("scallions",6,Ingredient.Measure.piece));
            VegiePho.addIngredients(new Ingredient("bean sprouts",(float)1.5,Ingredient.Measure.cup));
            VegiePho.addIngredients(new Ingredient("lime",1,Ingredient.Measure.piece));
            VegiePho.addSteps("To make broth: Place all the ingredients in pot with 8 cups of water");
            VegiePho.addSteps("Bring broth to a boil");
            VegiePho.addSteps("Strain broth and return to pot. Discard all solids");
            VegiePho.addSteps("To make pho: Cook rice noodles according to packet instructions. Drain them and rinse with cold water");
            VegiePho.addSteps("Ladle the broth over noodles");
            VegiePho.addSteps("Top with tofu, sprouts, onions or any additional ingredients to your liking! Enjoy!");
            allRecipe.add(VegiePho);

            Recipe GrilledChicken = new Recipe();
            GrilledChicken.setRecipeName("Grilled Chicken");
            GrilledChicken.setRecipeOrigin("American");
            GrilledChicken.setRecipeClass("Meat");
            GrilledChicken.setRecipeCategory("Main Dish");
            GrilledChicken.addIngredients(new Ingredient("skinless chicken",4, Ingredient.Measure.piece));
            GrilledChicken.addIngredients(new Ingredient("lemon juice",(float)0.5,Ingredient.Measure.cup));
            GrilledChicken.addIngredients(new Ingredient("onion powder",(float)0.5,Ingredient.Measure.tea_spoon));
            GrilledChicken.addIngredients(new Ingredient("black pepper",(float)0.4,Ingredient.Measure.tea_spoon));
            GrilledChicken.addIngredients(new Ingredient("salt",1,Ingredient.Measure.tea_spoon));
            GrilledChicken.addIngredients(new Ingredient("dried parsley",1,Ingredient.Measure.tea_spoon));
            GrilledChicken.addSteps("Preheat grill for medium high heat and lightly oil grate");
            GrilledChicken.addSteps("Dip chicken in lemon juice and sprinkle with all the ingredients listed above ");
            GrilledChicken.addSteps("Cook on grill for 10-15 minutes per side");
            GrilledChicken.addSteps("Serve and enjoy!");
            allRecipe.add(GrilledChicken);


















            Recipe BeefPho = new Recipe();
            BeefPho.setRecipeName("Beef Pho");
            allRecipe.add(BeefPho);



            Recipe BeefStew = new Recipe();
            BeefStew.setRecipeName("Beef Stew");
            allRecipe.add(BeefStew);

            Recipe BeefAndVegiePizza = new Recipe();
            BeefAndVegiePizza.setRecipeName("Beef and Vegie Pizza");
            allRecipe.add(BeefAndVegiePizza);

            Recipe IceCream = new Recipe();
            IceCream.setRecipeName("Ice Cream");
            allRecipe.add(IceCream);


            for(int i=0;i<allRecipe.size();i++){
                filterResult.add(allRecipe.get(i));
            }

        }
        if (filterResult.size()==allRecipe.size()) {
            reset();
        }
        else{
            showList.clear();
            for(int i=0;i<filterResult.size();i++){

                showList.add(filterResult.get(i).getRecipeName());

            }
            displayList(showList);
        }









        // just for test

        // read_jason();



        //This is how to add  items to list: use ArrayAdapter



        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterFunction(allRecipe,mEditText.getText().toString(),spClass.getSelectedItemPosition(),spOrigin.getSelectedItemPosition(),spCategory.getSelectedItemPosition());
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });

        // Do not change, this block is used to clear text on clicking the X botton

        //initially clear button is invisible
        mClearText.setVisibility(View.INVISIBLE);

        //clear button visibility on text change
        mEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                //do nothing
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0) {
                    mClearText.setVisibility(View.VISIBLE);
                } else {
                    mClearText.setVisibility(View.GONE);
                }
            }
        });
        recipeList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> adapterView,View view, int i, long l){

                goRecipe(i);
            }
        });
    }

    // This function is called to reset the text
    public void clear(View view) {
        mEditText.setText("");
        mClearText.setVisibility(View.GONE);
    }

    public void goRecipe(int i) {
        savedCategory=spCategory.getSelectedItemPosition();
        savedClass=spClass.getSelectedItemPosition();
        savedOrigin=spOrigin.getSelectedItemPosition();
        savedSearch=mEditText.getText().toString();
        int j=0;
        while(allRecipe.get(j).getRecipeName()!=showList.get(i)){
            j++;
        }
        Intent intent1 = new Intent(this,showRecipe.class );
        intent1.putExtra("RecipeNumber",j);
        startActivity(intent1);
    }

    public void goAdd(View view) {
        savedSearch="";
        savedOrigin=0;
        savedClass=0;
        savedCategory=0;
        Intent intentAdd = new Intent(this,editRecipe.class );
        int numOfNewRecipe=allRecipe.size();
        Recipe newRecipe=new Recipe();
        allRecipe.add(newRecipe);
        intentAdd.putExtra("RecipeNumber", numOfNewRecipe);
        intentAdd.putExtra("trueIfAdd",true);
        startActivity(intentAdd);
    }
    public void reset (){
        spClass.setSelection(0);
        spOrigin.setSelection(0);
        spCategory.setSelection(0);
        mEditText.setText("");
        mClearText.setVisibility(View.GONE);
        savedSearch="";
        savedOrigin=0;
        savedClass=0;
        savedCategory=0;
        filterResult=new LinkedList<>();
        for(int i=0;i<allRecipe.size();i++){
            filterResult.add(allRecipe.get(i));
        }
        showList=new LinkedList<>();
        for(int i=0;i<filterResult.size();i++){
            showList.add(filterResult.get(i).getRecipeName());
        }
        displayList(showList);

    }
    public void displayList(List<String> newList){
        ArrayAdapter newAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, newList);
        recipeList.setAdapter(newAdapter);
    }

    public  void read_jason(){
        String json = new String();
        //allRecipe = new LinkedList<>();
        //showList = new LinkedList<>();

        try {
            InputStream is = getAssets().open("test");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            System.out.println("Read from file"+json);
            if(!json.isEmpty()){
                JSONObject jsob= new JSONObject(json);
                JSONArray recipes;
                recipes=jsob.getJSONArray("recipes");
                for (int i =0; i <recipes.length(); i++) {
                    Recipe recpe = new Recipe();
                    List ingredient_list= new LinkedList<Ingredient>();
                    List steps_list= new LinkedList<String>();
                    JSONObject recp = recipes.getJSONObject(i);
                    String name = recp.getString("RecipeName");
                    showList.add(name);
                    recpe.setRecipeName(name);
                    String classs = recp.getString("Class");
                    recpe.setRecipeClass(classs);
                    String category = recp.getString("Category");
                    recpe.setRecipeCategory(category);
                    String Origin = recp.getString("Origin");
                    recpe.setRecipeOrigin(Origin);
                    JSONArray ject = recp.getJSONArray("newIngredients");
                    for (int a = 0; a < ject.length(); a++) {
                        JSONObject inget = ject.getJSONObject(i);
                        String names = inget.getString("name");
                        float quantity = inget.getLong("quantity");
                        String unit = inget.getString("unit");
                        Ingredient the_ingredient= new Ingredient();
                        the_ingredient.setIngName(names);
                        the_ingredient.setIngQuantity(quantity);
                        switch (unit) {
                            case "None":
                                the_ingredient.setIngUnits(Ingredient.Measure.none);
                                break;
                            case "cup":
                                the_ingredient.setIngUnits(Ingredient.Measure.cup);
                                break;
                            case "tea_spoon":
                                the_ingredient.setIngUnits(Ingredient.Measure.tea_spoon);
                                break;
                            case "table_spoon":
                                the_ingredient.setIngUnits(Ingredient.Measure.table_spoon);
                                break;
                            case "ounce":
                                the_ingredient.setIngUnits(Ingredient.Measure.ounce);
                                break;
                            case "kg":
                                the_ingredient.setIngUnits(Ingredient.Measure.kg);
                                break;
                            case "g":
                                the_ingredient.setIngUnits(Ingredient.Measure.g);
                                break;
                            case "piece":
                                the_ingredient.setIngUnits(Ingredient.Measure.piece);
                        }
                        ingredient_list.add(the_ingredient);

                    }

                    JSONArray sps = recp.getJSONArray("steps");
                    for (int b = 0; b < sps.length(); b++) {
                        JSONObject step = sps.getJSONObject(i);
                        String the_step=step.getString("step");
                        steps_list.add(the_step);
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }catch(java.io.IOException e){
            e.printStackTrace();

        }

    }

    public void onBackPressed()
    {
        if(backButtonCount >= 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }



    public List<Recipe> filterFunction (List<Recipe> allRecipeName,String searchText,int classOption, int originOption, int categoryOption){
        //we need this function to take a recipe list, and a search string
        //and output a filtered recipe list
        //we can input some recipe (see line 40-70)
        //and add attributes of recipes to test
        //the integer options are in the following order
//        Class {"Any","Beef", "Chicken", "Seafood", "Vegie"};
//        origin {"Any","Italian", "Chinese", "Midle Eastern", "Indian", "American"};
//        category {"Any","Starter", "Main Dish", "Desert", "Drink", "Sauce", "Salad"};


        showList = new LinkedList<>();
        filterResult= new LinkedList<>();
        //used to store filtered list



        //just for test,should show last 3 item with reversed order
        numOfFilteredRecipe=4;
        for (int i=0;i<numOfFilteredRecipe;i++){
            filterResult.add(allRecipe.get(allRecipe.size()-i-1));
        }


        for(int j=0;j<numOfFilteredRecipe;j++){
            showList.add(filterResult.get(j).getRecipeName());
        }






















        //please implement














        displayList(showList);//pass a string list to displaylist
        return filterResult;
    }



}
