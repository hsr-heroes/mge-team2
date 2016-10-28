package ch.hsr_heroes.gadgeothek;

import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.hsr_heroes.gadgeothek.domain.Gadget;
import ch.hsr_heroes.gadgeothek.service.Callback;
import ch.hsr_heroes.gadgeothek.service.LibraryService;

public class GadgetListActivity extends BaseListActivity {

    private GadgetAdapter gadgetAdapter;

    protected void onCreateMainContent(ViewGroup contentView) {
        super.onCreateMainContent(contentView);

        gadgetAdapter = new GadgetAdapter();
        recyclerView.setAdapter(gadgetAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayout.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        loadGadgets();
    }

    private void loadGadgets() {
        LibraryService.getGadgets(new Callback<List<Gadget>>() {
            @Override
            public void onCompletion(List<Gadget> input) {
                gadgetAdapter.setGadgetList(input);
                if(input.isEmpty()){
                    setEmptyMessage("No Gadgets at this School... Go Go Inspector Gadget =)");
                }else{
                    clearEmptyMessage();
                }
            }

            @Override
            public void onError(String message) {
                Toast.makeText(GadgetListActivity.this, "Error Loading Gadgets "+message, Toast.LENGTH_LONG).show();
                setEmptyMessage( "Error Loading Gadgets "+message);
            }
        });
    }

    @Override
    void refreshList() {
        loadGadgets();
    }

    private class GadgetAdapter extends RecyclerView.Adapter<GadgetAdapter.GadgetViewHolder>{

        private final List<Gadget> gadgets = new ArrayList<>();
        @Override
        public GadgetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new GadgetViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_gadget, parent, false));
        }

        @Override
        public void onBindViewHolder(GadgetViewHolder holder, int position) {
            final Gadget g = gadgets.get(position);
            holder.name.setText(g.getName());
            holder.manufacturer.setText(g.getManufacturer());

            holder.buttonReserve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LibraryService.reserveGadget(g, new Callback<Boolean>() {
                        @Override
                        public void onCompletion(Boolean reserved) {
                            if(reserved) {
                                startActivity(new Intent(GadgetListActivity.this, MyReservationsActivity.class));
                                Toast.makeText(GadgetListActivity.this, R.string.reservation_successful, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(GadgetListActivity.this, R.string.reservation_failed, Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(GadgetListActivity.this, getString(R.string.unable_to_reserve_gadget) + "\n" + message, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }

        @Override
        public int getItemCount() {
            return gadgets.size();
        }

        public void setGadgetList(List<Gadget> input) {
            gadgets.clear();
            gadgets.addAll(input);
            notifyDataSetChanged();
        }

        class GadgetViewHolder extends RecyclerView.ViewHolder{
            private final TextView name;
            private final TextView manufacturer;
            private final ImageView buttonReserve;

            public GadgetViewHolder(View itemView) {
                super(itemView);
                this.name = (TextView) itemView.findViewById(R.id.my_gadget_name);
                this.manufacturer = (TextView) itemView.findViewById(R.id.gadget_manufacturer);
                this.buttonReserve = (ImageView) itemView.findViewById(R.id.button_reserve);
            }
        }

    }
}
