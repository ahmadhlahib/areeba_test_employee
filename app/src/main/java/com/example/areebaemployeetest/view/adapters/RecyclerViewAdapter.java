package com.example.areebaemployeetest.view.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.areebaemployeetest.R;
import com.example.areebaemployeetest.view.AddEditEmployeeActivity;
import com.example.areebaemployeetest.view.IEmployee;
import com.example.areebaemployeetest.view.dialog.ItemDetailsDialog;
import com.example.areebaemployeetest.model.data.Employee;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.Holder>  {
	private Activity activity;
	private List<Employee> searchArray;
	private List<Employee> originalArray;
	private IEmployee.IOnSelectedEmployeeListener listener;
	public List<Employee> selectedEmployees =new ArrayList<>();

	public RecyclerViewAdapter(Activity activity,List<Employee> arraylist) {
		this.activity=activity;
		this.searchArray = new ArrayList<Employee>();
		this.originalArray = arraylist;
		this.searchArray.addAll(arraylist);
	}


	@Override
	public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.single_item_listview, parent, false);
		return getViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(Holder holder, @SuppressLint("RecyclerView") int position) {
		try
		{
			Employee item = searchArray.get(position);
			setUpViewHolder(item, holder);
			holder.itemView.setOnClickListener(new View.OnClickListener() {
				@Override public void onClick(View v) {
					Employee item = searchArray.get(position);
					if(!item.isSelected())
						showItemDetailsDialog(item);
				}
			});
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			Toast.makeText(activity, ex.getStackTrace() + "", Toast.LENGTH_LONG).show();
		}
	}

	private void showItemDetailsDialog(Employee item) {
		try {
			ItemDetailsDialog dialog = new ItemDetailsDialog(activity,item);
			dialog.show();
		}catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {
		return searchArray.size();
	}
	public List<Employee> getItems() {
		return originalArray;
	}
	/**
	 * @param v
	 * @return
	 */
	private Holder getViewHolder(View v) {
		Holder holder;
		holder = new Holder(v, R.id.tv_name,
				R.id.tv_email,
				R.id.tv_phone,
				R.id.tv_gender,
				R.id.iv_image,
				R.id.iv_edit,
				R.id.ll_employee_box
				);
		return holder;
	}

	private void setUpViewHolder(final Employee item,
			Holder holder) throws Exception {
		try {
			holder.setValues(item,item.getPicture().getLarge());
		}catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}

	public void setList(List<Employee> list, IEmployee.IOnSelectedEmployeeListener listener) {
		searchArray.clear();
		originalArray=list;
		searchArray.addAll(list);
		this.listener=listener;
		listener.OnGettingDataComplete();
		notifyDataSetChanged();
	}

	public void filter(String key) {
		List<Employee> searchResults = new ArrayList<>();
		// Implement the logic to filter by the search query
		for (Employee item : originalArray) {
			if (item.getName()!=null && item.getName().getFirst()!=null && item.getName().getFirst().toLowerCase().contains(key.toLowerCase())
					|| item.getName()!=null && item.getName().getLast()!=null && item.getName().getLast().toLowerCase().contains(key.toLowerCase())
					|| item.getName()!=null && item.getName().getTitle()!=null && item.getName().getTitle().toLowerCase().contains(key.toLowerCase())
					|| item.getLocation()!=null && item.getLocation().getCity()!=null && item.getLocation().getCity().toLowerCase().contains(key.toLowerCase())
					|| item.getLocation()!=null && item.getLocation().getCountry()!=null && item.getLocation().getCountry().toLowerCase().contains(key.toLowerCase())
					|| item.getLocation()!=null && item.getLocation().getState()!=null && item.getLocation().getState().toLowerCase().contains(key.toLowerCase())
					|| item.getLocation()!=null && item.getLocation().getStreet()!=null &&( item.getLocation().getStreet().getName().toLowerCase().contains(key.toLowerCase()) || (item.getLocation().getStreet().getNumber()+"").contains(key.toLowerCase()))
					|| item.getCell()!=null && item.getCell().toLowerCase().contains(key.toLowerCase())) {
				searchResults.add(item);
			}
		}
		searchArray.clear();
		searchArray.addAll(searchResults);
		notifyDataSetChanged();
	}

	public void removeSelectedEmployee() {
		originalArray.removeAll(selectedEmployees);
		searchArray.removeAll(selectedEmployees);
		notifyDataSetChanged();
	}

	public void removeAllEmployee() {
		originalArray=new ArrayList<>();
		searchArray=new ArrayList<>();
		notifyDataSetChanged();
	}

	public class Holder extends RecyclerView.ViewHolder {
		private TextView tv_name, tv_email, tv_phone;
		private ImageView ivImage,ivEdit;
		private LinearLayout ll_employee_box;

		public Holder(View v, int usernameId, int emailId, int phoneId, int genderId,int ivImageId,int ivEditId,int ll_employee_box) {
			super(v);
			this.tv_name = (TextView) v.findViewById(usernameId);
			this.tv_email = (TextView) v.findViewById(emailId);
			this.tv_phone = (TextView) v.findViewById(phoneId);
			this.ivImage = (ImageView) v.findViewById(ivImageId);
			this.ivEdit = (ImageView) v.findViewById(ivEditId);
			this.ll_employee_box = (LinearLayout) v.findViewById(ll_employee_box);
		}

		private void setValues( Employee employee,String imageUrl) {
			String fullName=employee.getName().getTitle()+" "+employee.getName().getFirst()+" "+employee.getName().getLast();
			this.tv_name.setText(fullName);
			this.tv_email.setText(employee.getEmail());
			this.tv_phone.setText(employee.getPhone());
			try{
				if(imageUrl!=null && !imageUrl.isEmpty())
				{
					Picasso.with(activity).load(imageUrl).fit().centerCrop()
							.placeholder(R.drawable.placeholder)
							.error(R.drawable.error)
							.into(this.ivImage);
				}
				else {
					if(employee.getGender().equalsIgnoreCase("male"))
						Picasso.with(activity).load(R.drawable.male).fit().centerCrop()
								.placeholder(R.drawable.placeholder)
								.error(R.drawable.error)
								.into(this.ivImage);
					else
						Picasso.with(activity).load(R.drawable.female).fit().centerCrop()
								.placeholder(R.drawable.placeholder)
								.error(R.drawable.error)
								.into(this.ivImage);
				}
			}catch (Exception exception)
			{
				exception.printStackTrace();
			}
			if(employee.isSelected())
			{
				this.ll_employee_box.setBackgroundResource(R.drawable.selected_shape_border);
				ivEdit.setVisibility(View.GONE);
			}
			else
			{
				this.ll_employee_box.setBackgroundResource(R.drawable.shape_border1);
				ivEdit.setVisibility(View.VISIBLE);
			}
			this.ll_employee_box.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View view) {
					employee.setSelected(!employee.isSelected());
					if(employee.isSelected())
					{
						ll_employee_box.setBackgroundResource(R.drawable.selected_shape_border);
						selectedEmployees.add(employee);
						ivEdit.setVisibility(View.GONE);
					}
					else
					{
						ll_employee_box.setBackgroundResource(R.drawable.shape_border1);
						for (Employee selectedEmployee : selectedEmployees) {
							if(selectedEmployee.getLogin().getUuid().equalsIgnoreCase(employee.getLogin().getUuid()))
							{
								selectedEmployees.remove(selectedEmployee);
								break;
							}
						}
						ivEdit.setVisibility(View.VISIBLE);
					}
					if(selectedEmployees.size()>0)
						listener.OnEmployeeSelected(true);
					else
						listener.OnEmployeeSelected(false);
					return true;
				}
			});
			ivEdit.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(activity, AddEditEmployeeActivity.class);
					intent.putExtra("UUID",employee.getLogin().getUuid());
					activity.startActivity(intent);
				}
			});
		}
	}
}
