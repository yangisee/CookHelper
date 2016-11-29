package group88.cookhelper;

/**
 * Created by YANG on 2016-11-13.
 */

import android.app.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;

import android.content.Context;

import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.MalformedInputException;
import java.util.LinkedList;
import java.util.List;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.nio.channels.FileChannel;



public class editRecipe extends Activity {

    public String[] spinnerMeasure = {"none","cup", "tea spoon", "table spoon", "ounce", "kg", "g", "piece"};
    private String[] spinnerAddClass = {"Any","Beef", "Chicken", "Seafood", "Vegie"};
    private String[] spinnerAddOrigin = {"Any","Italian", "Chinese", "Midle Eastern", "Indian", "American"};
    private String[] spinnerAddCategory = {"Any","Starter", "Main Dish", "Desert", "Drink", "Sauce", "Salad"};
    private List<Ingredient> newIngredientList = new LinkedList<>();
    private List<String> newStepList = new LinkedList<>();
    private List<String> ingList = new LinkedList<>();
    private List<String> stepList = new LinkedList<>();
    private Recipe newRecipe = new Recipe();


    EditText mEditText;
    Button mClearText;
    Button mSave;
    Button mAddIng;
    Button mAddStep;
    ListView editIngList;
    ListView editStepList;

    int stepCounter = 1;
    Spinner aClass;
    Spinner aOrigin;
    Spinner aCategory;
    AlertDialog.Builder dialogBuilder;
    String newStep;
    Ingredient newIng = new Ingredient();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_recipe);

        Intent intent = getIntent();
        newRecipe =(Recipe ) intent.getSerializableExtra("Recipe");


        mEditText=(EditText) findViewById(R.id.EditName);
        mEditText.setText(newRecipe.getRecipeName());
        mAddIng = (Button) findViewById(R.id.addIng);
        mAddIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingredientDialog();
            }
        });
        mAddStep =(Button) findViewById(R.id.addStep);
        mAddStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepDialog();
            }
        });
        aClass = (Spinner) findViewById(R.id.Addclass);
        aOrigin = (Spinner) findViewById(R.id.Addorigin);
        aCategory = (Spinner) findViewById(R.id.Addcategory);
        ArrayAdapter<String> adapterAClass = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerAddClass);
        aClass.setAdapter(adapterAClass);
        //        private String[] spinnerClass ={"Any","Beef", "Chicken", "Seafood", "Vegie"};
//        private String[] spinnerOrigin= {"Any","Italian", "Chinese", "Midle Eastern", "Indian", "American"};
//        private String[] spinnerCategory= {"Any","Starter", "Main Dish", "Desert", "Drink", "Sauce", "Salad"};

        switch (newRecipe.getRecipeClass()) {
            case "Any":
                aClass.setSelection(0);
                break;
            case "Beef":
                aClass.setSelection(0);
                break;
            case "Chicken":
                aClass.setSelection(0);
                break;
            case "Seafood":
                aClass.setSelection(0);;
                break;
            case "Vegie":
                aClass.setSelection(0);
                break;
            case "kg":
                aClass.setSelection(0);
                break;
            case "g":
                aClass.setSelection(0);
                break;
            case "piece":
                aClass.setSelection(0);
        }



        ArrayAdapter<String> adapteraOrigin = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerAddOrigin);
        aOrigin.setAdapter(adapteraOrigin);
        aOrigin.setSelection(0);

        ArrayAdapter<String> adapteraCategory = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerAddCategory);
        aCategory.setAdapter(adapteraCategory);
        aCategory.setSelection(0);

        editIngList =(ListView) findViewById(R.id.edit_ing_list);
        ArrayAdapter adapterIng = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ingList);
        editIngList.setAdapter(adapterIng);

        editStepList = (ListView) findViewById(R.id.edit_step_list);
        ArrayAdapter adapterStep = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stepList);
        editStepList.setAdapter(adapterStep);



        // Do not change, this block is used to clear text on clicking the X botton

        mClearText = (Button) findViewById(R.id.clearText);
        mSave = (Button) findViewById(R.id.saveEdit);

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goRecipe();
            }
        });
        //initially clear button is invisible
        mClearText.setVisibility(View.INVISIBLE);
        mEditText = (EditText) findViewById(R.id.EditName);

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
                if (s.length() != 0) {
                    mClearText.setVisibility(View.VISIBLE);
                } else {
                    mClearText.setVisibility(View.GONE);
                }
            }
        });
    }

    public void clearName(View view) {
        mEditText.setText("");
        mClearText.setVisibility(View.GONE);
    }


    public void ingredientDialog(){
        final EditText nameInput ;
        final EditText quantityInput;
        final Spinner unitSelection;
        Button btnok;
        Button btncancel;
        final Dialog dialogCustom = new Dialog(this);

        dialogCustom.setContentView(R.layout.dialog);
        dialogCustom.show();
        dialogCustom.setTitle("Add your ingredient:");
        nameInput = (EditText) dialogCustom.findViewById(R.id.editIngName);
        quantityInput = (EditText) dialogCustom.findViewById(R.id.editIngQ);
        unitSelection =(Spinner) dialogCustom.findViewById(R.id.editIngU);
        ArrayAdapter<String> adapterMeasure = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerMeasure);
        unitSelection.setAdapter(adapterMeasure);
        unitSelection.setSelection(0);
        btnok = (Button) dialogCustom.findViewById(R.id.OKing);
        btncancel = (Button) dialogCustom.findViewById(R.id.Canceling);
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!nameInput.getText().toString().trim().isEmpty()){
                    if(!quantityInput.getText().toString().trim().isEmpty()){
                        newIng.setIngName(nameInput.getText().toString());
                        newIng.setIngQuantity(Float.valueOf(quantityInput.getText().toString()));
                        switch (unitSelection.getSelectedItemPosition()){
                            case 0:
                                newIng.setIngUnits(Ingredient.Measure.none);
                                break;
                            case 1:
                                newIng.setIngUnits(Ingredient.Measure.cup);
                                break;
                            case 2:
                                newIng.setIngUnits(Ingredient.Measure.tea_spoon);
                            case 3:
                                newIng.setIngUnits(Ingredient.Measure.table_spoon);
                                break;
                            case 4:
                                newIng.setIngUnits(Ingredient.Measure.ounce);
                                break;
                            case 5:
                                newIng.setIngUnits(Ingredient.Measure.kg);
                                break;
                            case 6:
                                newIng.setIngUnits(Ingredient.Measure.g);
                                break;
                            case 7:
                                newIng.setIngUnits(Ingredient.Measure.piece);
                                break;
                        }
                        newIngredientList.add(newIng);
                        ingList.add(newIng.getIngName()+" x " + newIng.getIngQuantity()+" "+newIng.getIngUnits());
                        display();

                        dialogCustom.dismiss();
                    }
                }
                dialogCustom.dismiss();
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogCustom.dismiss();
            }
        });
    }

    public void stepDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final EditText stepInput = new EditText(this);

        dialogBuilder.setTitle("Enter your step "+stepCounter);
        dialogBuilder.setMessage("Type your step here:");
        dialogBuilder.setView(stepInput);
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!stepInput.getText().toString().trim().isEmpty()){

                    newStep = stepCounter +": "+ stepInput.getText().toString();
                    newStepList.add(newStep);
                    display();
                    stepCounter++;
                   }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialogBuilder.show();
    }
    public void display(){
        editIngList =(ListView) findViewById(R.id.edit_ing_list);
        ArrayAdapter adapterIng = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ingList);
        editIngList.setAdapter(adapterIng);

        editStepList = (ListView) findViewById(R.id.edit_step_list);
        ArrayAdapter adapterStep = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stepList);
        editStepList.setAdapter(adapterStep);
    }



    public void saveEditRecipe(View view) {
   try {
       read_jason();
                JSONObject data = new JSONObject();
                JSONArray recipes = new JSONArray();
                data.put("recipes", recipes);
       MainActivity.allRecipe.add(newRecipe);
       for(int c=0;c<MainActivity.allRecipe.size(); c++) {
           JSONObject jasonRecipe = new JSONObject();
           jasonRecipe.put("RecipeName", MainActivity.allRecipe.get(c).getRecipeName());
           jasonRecipe.put("Class", MainActivity.allRecipe.get(c).getRecipeClass());
           jasonRecipe.put("Category", MainActivity.allRecipe.get(c).getRecipeCategory());
           jasonRecipe.put("Origin", MainActivity.allRecipe.get(c).getRecipeOrigin());
           JSONArray newIngredients = new JSONArray();
           int i = 0;
           while (i < MainActivity.allRecipe.get(c).getIngredients().size()) {
               JSONObject ingred = new JSONObject();
               ingred.put("name", MainActivity.allRecipe.get(c).getIngredients().get(i).getIngName());
               ingred.put("quantity", MainActivity.allRecipe.get(c).getIngredients().get(i).getIngQuantity());
               ingred.put("unit", MainActivity.allRecipe.get(c).getIngredients().get(i).getIngUnits());
               newIngredients.put(ingred);
               i++;
           }
           jasonRecipe.put("Ingredients", newIngredients);
           JSONArray stps = new JSONArray();
           int a = 0;
           while (a < MainActivity.allRecipe.get(c).getSteps().size()) {
               JSONObject sp = new JSONObject();
               sp.put("step", MainActivity.allRecipe.get(c).getSteps().get(a));
               stps.put(sp);
           }
           jasonRecipe.put("stpes", stps);
            recipes.put(jasonRecipe);
       }
       OutputStreamWriter write = new OutputStreamWriter(openFileOutput("test", Context.MODE_PRIVATE));
       write.write(data.toString());
       write.close();
            }catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
     catch(JSONException e) {
                e.printStackTrace();
            }
        }


    public void goRecipe() {
        newRecipe.setRecipeName(mEditText.getText().toString());
        newRecipe.setRecipeClass(spinnerAddClass[aClass.getSelectedItemPosition()]);
        newRecipe.setRecipeOrigin(spinnerAddOrigin[aOrigin.getSelectedItemPosition()]);
        newRecipe.setRecipeCategory(spinnerAddCategory[aCategory.getSelectedItemPosition()]);
        newRecipe.setIngredients(newIngredientList);
        newRecipe.setSteps(stepList);
        Intent intentShow = new Intent(this,showRecipe.class );
        intentShow.putExtra("Recipe", newRecipe);
        startActivity(intentShow);
    }
    public  void read_jason(){
        String json = new String();
        MainActivity.allRecipe = new LinkedList<>();

        try {
            InputStream is = getAssets().open("test");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            JSONObject jsob= new JSONObject(json);
            JSONArray recipes= new JSONArray();
            recipes=jsob.getJSONArray("recipes");
            for (int i =0; i <recipes.length(); i++) {
                Recipe recpe = new Recipe();
                List ingredient_list= new LinkedList<Ingredient>();
                List steps_list= new LinkedList<String>();
                JSONObject recp = recipes.getJSONObject(i);
                String name = recp.getString("RecipeName");
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
                recpe.setIngredients(ingredient_list);
                JSONArray sps = recp.getJSONArray("steps");
                for (int b = 0; b < sps.length(); b++) {
                    JSONObject step = sps.getJSONObject(i);
                    String the_step=step.getString("step");
                    steps_list.add(the_step);
                }
                recpe.setIngredients(steps_list);
                MainActivity.allRecipe.add(recpe);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }catch(java.io.IOException e){
            e.printStackTrace();
        }
    }
}



